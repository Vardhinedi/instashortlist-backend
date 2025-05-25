package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.repository.ApplicationRepository;
import com.instashortlist.backend.utils.ResumeTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationRepository repository;

    private static final Map<String, List<String>> expectedSkills = Map.of(
            "java developer", List.of("java", "spring", "hibernate", "rest", "mysql"),
            "frontend developer", List.of("html", "css", "javascript", "react", "tailwind"),
            "data analyst", List.of("sql", "excel", "python", "powerbi", "tableau")
    );

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("✅ Reactive controller is working!");
    }

    @PostMapping(value = "/apply", consumes = "multipart/form-data")
    public Mono<ResponseEntity<?>> apply(
            @RequestPart("resume") Mono<FilePart> resumeMono,
            @RequestPart("name") Mono<String> nameMono,
            @RequestPart("email") Mono<String> emailMono,
            @RequestPart("jobRole") Mono<String> jobRoleMono
    ) {
        return Mono.zip(resumeMono, nameMono, emailMono, jobRoleMono)
                .flatMap(tuple -> {
                    FilePart resume = tuple.getT1();
                    String name = tuple.getT2();
                    String email = tuple.getT3();
                    String jobRole = tuple.getT4().toLowerCase().trim();

                    String matchedRole = expectedSkills.keySet().stream()
                            .filter(jobRole::contains)
                            .findFirst()
                            .orElse(null);

                    if (matchedRole == null) {
                        return Mono.just(ResponseEntity.ok(Map.of(
                                "status", "rejected",
                                "reason", "Job role not supported."
                        )));
                    }

                    List<String> requiredSkills = expectedSkills.get(matchedRole);

                    return ResumeTextExtractor.extractTextFromPdf(resume)
                            .flatMap(resumeText -> {
                                String resumeLower = resumeText.toLowerCase();
                                List<String> matchedSkills = requiredSkills.stream()
                                        .filter(resumeLower::contains)
                                        .toList();
                                List<String> missingSkills = requiredSkills.stream()
                                        .filter(skill -> !resumeLower.contains(skill))
                                        .toList();

                                int score = (int) (((double) matchedSkills.size() / requiredSkills.size()) * 100);
                                String status = (score >= 40) ? "shortlisted" : "rejected";
                                String reason;
                                if (status.equals("shortlisted")) {
                                    reason = "✅ Shortlisted! Match Score: " + score + "%";
                                } else if (matchedSkills.isEmpty()) {
                                    reason = "❌ Rejected. Resume lacks all required skills.";
                                } else if (score < 25) {
                                    reason = "❌ Rejected. Very few matching skills.";
                                } else {
                                    reason = "❌ Rejected. Missing key skills: " + String.join(", ", missingSkills);
                                }

                                Application app = new Application();
                                app.setName(name);
                                app.setEmail(email);
                                app.setJobRole(jobRole);
                                app.setMatchedSkills(matchedSkills);
                                app.setMissingSkills(missingSkills);
                                app.setMatchScore(score);
                                app.setStatus(status);
                                app.setReason(reason);
                                app.setCreatedAt(LocalDateTime.now());

                                return repository.save(app)
                                        .thenReturn(ResponseEntity.ok(Map.of(
                                                "status", status,
                                                "score", score,
                                                "matchedSkills", matchedSkills,
                                                "missingSkills", missingSkills,
                                                "reason", reason
                                        )));
                            });
                });
    }
}