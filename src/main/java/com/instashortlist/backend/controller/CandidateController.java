package com.instashortlist.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instashortlist.backend.dto.CandidateResponse;
import com.instashortlist.backend.model.Candidate;
import com.instashortlist.backend.service.CandidateService;
import com.instashortlist.backend.service.CandidateStepService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@Validated
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateStepService candidateStepService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<Map<String, Object>>> createWithFile(
            @RequestPart("candidate") Mono<String> candidateJsonMono,
            @RequestPart(value = "file", required = false) Mono<FilePart> fileMono
    ) {
        return candidateJsonMono
                .flatMap(candidateJson -> {
                    try {
                        Candidate candidate = objectMapper.readValue(candidateJson, Candidate.class);

                        if (candidate.getAppliedDate() == null && candidate.getAppliedDateStr() != null) {
                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                candidate.setAppliedDate(LocalDate.parse(candidate.getAppliedDateStr(), formatter));
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Invalid appliedDate format: " + candidate.getAppliedDateStr());
                            }
                        }

                        return fileMono
                                .flatMap(file -> candidateService.createCandidateWithFile(candidate, file))
                                .switchIfEmpty(Mono.just(candidate))
                                .flatMap(saved ->
                                    candidateStepService.createCandidateStepsFromAssessments(saved.getId(), saved.getJobId())
                                        .thenReturn(saved)
                                );
                    } catch (Exception e) {
                        return Mono.error(new IllegalArgumentException("Invalid candidate JSON: " + e.getMessage()));
                    }
                })
                .map(saved -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("data", saved);
                    return ResponseEntity.status(201).body(response);
                })
                .onErrorResume(e -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "error");
                    response.put("errors", Map.of("message", e.getMessage()));
                    return Mono.just(ResponseEntity.badRequest().body(response));
                });
    }

    @GetMapping
    public Flux<CandidateResponse> getAll() {
        return candidateService.getAllCandidateResponses();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> getById(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Candidate>> update(@PathVariable Long id, @Valid @RequestBody Candidate updated) {
        return candidateService.updateCandidate(id, updated)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return candidateService.deleteCandidate(id)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }

    @GetMapping("/{id}/resume")
    public Mono<ResponseEntity<byte[]>> downloadResume(@PathVariable Long id) {
        return candidateService.getCandidateById(id)
                .flatMap(candidate -> {
                    ByteBuffer buffer = candidate.getAttachments();

                    if (buffer == null || buffer.remaining() == 0) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new byte[0]));
                    }

                    byte[] pdfData = new byte[buffer.remaining()];
                    buffer.get(pdfData);
                    buffer.rewind();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(ContentDisposition.attachment()
                            .filename("resume_" + id + ".pdf")
                            .build());
                    headers.setContentLength(pdfData.length);

                    return Mono.just(ResponseEntity.ok().headers(headers).body(pdfData));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new byte[0])));
    }
}
