package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.repository.ApplicationRepository;
import com.instashortlist.backend.utils.ResumeTextExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository repository;

    private static final Map<String, List<String>> expectedSkills = Map.of(
            "java developer", List.of("java", "spring", "hibernate", "rest", "mysql"),
            "frontend developer", List.of("html", "css", "javascript", "react", "tailwind"),
            "data analyst", List.of("sql", "excel", "python", "powerbi", "tableau")
    );

    public Mono<Application> processApplication(FilePart resume, String name, String email, String jobRole) {
        String jobRoleLower = jobRole.toLowerCase().trim();

        String matchedRole = expectedSkills.keySet().stream()
                .filter(jobRoleLower::contains)
                .findFirst()
                .orElse(null);

        if (matchedRole == null) {
            Application app = new Application();
            app.setName(name);
            app.setEmail(email);
            app.setJobRole(jobRoleLower);
            app.setStatus("rejected");
            app.setReason("Job role not supported.");
            app.setCreatedAt(LocalDateTime.now());
            return repository.save(app);
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
                    app.setJobRole(jobRoleLower);
                    app.setMatchedSkills(String.join(",", matchedSkills));
                    app.setMissingSkills(String.join(",", missingSkills));
                    app.setMatchScore(score);
                    app.setStatus(status);
                    app.setReason(reason);
                    app.setCreatedAt(LocalDateTime.now());

                    return repository.save(app);
                });
    }
}
