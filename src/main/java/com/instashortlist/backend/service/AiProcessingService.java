package com.instashortlist.backend.service;

     import com.instashortlist.backend.model.Assessment;
     import com.instashortlist.backend.repository.AssessmentRepository;
     import com.instashortlist.backend.repository.JobRepository;
     import org.apache.pdfbox.pdmodel.PDDocument;
     import org.apache.pdfbox.text.PDFTextStripper;
     import org.springframework.beans.factory.annotation.Autowired;
     import org.springframework.stereotype.Service;
     import reactor.core.publisher.Mono;

     import java.nio.ByteBuffer;
     import java.util.Arrays;
     import java.util.List;

     @Service
     public class AiProcessingService {

         @Autowired
         private AssessmentRepository assessmentRepository;

         @Autowired
         private JobRepository jobRepository;

         public Mono<Assessment> parseResumeAndUpdateScore(Long jobId, ByteBuffer pdfData) {
             String resumeText = extractTextFromPdf(pdfData); // Use PDFBox for extraction
             int score = calculateAiScore(resumeText); // AI-like scoring
             return assessmentRepository.findByJobIdAndStepOrder(jobId, 1)
                     .switchIfEmpty(Mono.defer(() -> {
                         Assessment a = new Assessment();
                         a.setStepOrder(1);
                         a.setStepName("Resume Screening");
                         a.setMode("AI");
                         a.setIsAiEnabled(true);
                         a.setPassingCriteria(70);
                         return assessmentRepository.save(a);
                     }))
                     .flatMap(a -> {
                         a.setScore(score);
                         return assessmentRepository.save(a);
                     })
                     .doOnSuccess(a -> jobRepository.updateHasAssessmentsLinked(jobId, true));
         }

         // Extract text using PDFBox
         private String extractTextFromPdf(ByteBuffer pdfData) {
             try {
                 byte[] bytes = new byte[pdfData.remaining()];
                 pdfData.get(bytes);
                 PDDocument document = PDDocument.load(bytes);
                 PDFTextStripper stripper = new PDFTextStripper();
                 String text = stripper.getText(document).toLowerCase();
                 document.close();
                 return text;
             } catch (Exception e) {
                 throw new RuntimeException("PDF parsing error: " + e.getMessage());
             }
         }

         // AI-like scoring based on resume content
         private int calculateAiScore(String resumeText) {
             List<String> requiredSkills = Arrays.asList("java", "spring", "experience", "developer");
             long matches = requiredSkills.stream().filter(resumeText::contains).count();
             return (int) (((double) matches / requiredSkills.size()) * 100); // Score out of 100
         }
     }