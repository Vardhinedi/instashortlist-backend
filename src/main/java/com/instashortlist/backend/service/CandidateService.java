package com.instashortlist.backend.service;

import com.instashortlist.backend.dto.AttachmentDTO;
import com.instashortlist.backend.dto.CandidateResponse;
import com.instashortlist.backend.model.Assessment;
import com.instashortlist.backend.model.Candidate;
import com.instashortlist.backend.model.CandidateStep;
import com.instashortlist.backend.repository.AssessmentRepository;
import com.instashortlist.backend.repository.CandidateRepository;
import com.instashortlist.backend.repository.CandidateStepRepository;
import com.instashortlist.backend.repository.JobRepository;
import com.instashortlist.backend.util.ResumeMatcherClient;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private CandidateStepRepository candidateStepRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ResumeMatcherClient resumeMatcherClient;

    public Flux<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Flux<CandidateResponse> getAllCandidateResponses() {
        return candidateRepository.findAll()
                .map(candidate -> {
                    CandidateResponse dto = new CandidateResponse();
                    dto.setId(candidate.getId());
                    dto.setName(candidate.getName());
                    dto.setEmail(candidate.getEmail());
                    dto.setPhone(candidate.getPhone());
                    dto.setRole(candidate.getRole());
                    dto.setAppliedRole(candidate.getAppliedRole());
                    dto.setDepartment(candidate.getDepartment());
                    dto.setEmploymentType(candidate.getEmploymentType());
                    dto.setWorkType(candidate.getWorkType());
                    dto.setStatus(candidate.getStatus());
                    dto.setScore(candidate.getScore());
                    dto.setPosition(candidate.getPosition());
                    dto.setExperience(candidate.getExperience());
                    dto.setJobId(candidate.getJobId());

                    if (candidate.getAppliedDate() != null) {
                        dto.setAppliedDate(candidate.getAppliedDate().toString());
                    }

                    if (candidate.getAttachments() != null && candidate.getAttachments().remaining() > 0) {
                        byte[] bytes = new byte[candidate.getAttachments().remaining()];
                        candidate.getAttachments().duplicate().get(bytes);
                        String base64 = Base64.getEncoder().encodeToString(bytes);
                        dto.setAttachment(new AttachmentDTO(
                                "resume_" + candidate.getId() + ".pdf",
                                "application/pdf",
                                base64
                        ));
                    }

                    return dto;
                });
    }

    public Mono<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }

    public Mono<Candidate> createCandidateWithFile(Candidate candidate, FilePart file) {
        return DataBufferUtils.join(file.content())
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return bytes;
                })
                .flatMap(bytes -> {
                    candidate.setAttachments(ByteBuffer.wrap(bytes));

                    // ✅ Extract text from resume PDF
                    return extractTextFromPdf(bytes)
                            .flatMap(resumeText ->
                                    resumeMatcherClient.getMatchScore(
                                            resumeText,
                                            candidate.getAppliedRole() != null ? candidate.getAppliedRole() : ""
                                    )
                            )
                            .flatMap(result -> {
                                // ✅ Set match_score into candidate.score
                                Double score = (Double) result.get("match_score");
                                candidate.setScore(score.intValue());
                                return candidateRepository.save(candidate);
                            });
                })
                .flatMap(savedCandidate -> {
                    Long jobId = savedCandidate.getJobId();
                    Long candidateId = savedCandidate.getId();
                    AtomicInteger counter = new AtomicInteger(0);

                    Mono<Void> steps = assessmentRepository.findByJobIdOrderByStepOrderAsc(jobId)
                            .flatMap(assessment -> {
                                CandidateStep step = new CandidateStep();
                                step.setCandidateId(candidateId);
                                step.setAssessmentId(assessment.getId());
                                step.setStepOrder(assessment.getStepOrder());
                                step.setStepName(assessment.getQuestion());

                                if (counter.getAndIncrement() == 0) {
                                    step.setStatus("IN_PROGRESS");
                                } else {
                                    step.setStatus("PENDING");
                                }

                                step.setCompleted(false);
                                return candidateStepRepository.save(step);
                            })
                            .then();

                    Mono<Void> updateJob = jobRepository.findById(jobId)
                            .flatMap(job -> {
                                job.setApplicants(job.getApplicants() + 1);
                                return jobRepository.save(job);
                            })
                            .then();

                    return Mono.when(steps, updateJob).thenReturn(savedCandidate);
                });
    }

    public Mono<Candidate> updateCandidate(Long id, Candidate updated) {
        return candidateRepository.findById(id)
                .flatMap(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    existing.setPhone(updated.getPhone());
                    existing.setRole(updated.getRole());
                    existing.setAppliedRole(updated.getAppliedRole());
                    existing.setDepartment(updated.getDepartment());
                    existing.setEmploymentType(updated.getEmploymentType());
                    existing.setWorkType(updated.getWorkType());
                    existing.setAppliedDate(updated.getAppliedDate());
                    existing.setStatus(updated.getStatus());
                    existing.setScore(updated.getScore());
                    existing.setPosition(updated.getPosition());
                    existing.setExperience(updated.getExperience());
                    existing.setAttachments(updated.getAttachments());
                    existing.setJobId(updated.getJobId());
                    return candidateRepository.save(existing);
                });
    }

    public Mono<Void> deleteCandidate(Long id) {
        return candidateRepository.findById(id)
                .flatMap(candidateRepository::delete);
    }

    // ✅ Extract resume text using PDFBox
    private Mono<String> extractTextFromPdf(byte[] pdfBytes) {
        try {
            PDDocument document = PDDocument.load(pdfBytes);
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();
            return Mono.just(text);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("PDF parsing error: " + e.getMessage()));
        }
    }
}
