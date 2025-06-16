package com.instashortlist.backend.service;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
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

    // ✅ Save resume file to disk and return path
    public Mono<String> saveResumeFile(FilePart filePart) {
        String path = "uploads/" + System.currentTimeMillis() + "-" + filePart.filename();
        File targetFile = new File(path);

        return filePart.transferTo(targetFile)
                .then(Mono.just(targetFile.getAbsolutePath()));
    }

    // ✅ Save application with status "pending"
    public Mono<Application> save(Application application) {
        return repository.save(application);
    }

    // (Optional) Process application directly on upload - NOT used for Quartz
    public Mono<Application> processNow(FilePart resume, String name, String email, String jobRole) {
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

        return resume
                .transferTo(new File("temp_resume.pdf"))
                .then(Mono.fromCallable(() -> com.instashortlist.backend.util.ResumeTextExtractor.extractText(new File("temp_resume.pdf"))))
                .flatMap(resumeText -> {
                    String resumeLower = resumeText.toLowerCase();
                    List<String> matched = requiredSkills.stream().filter(resumeLower::contains).toList();
                    List<String> missing = requiredSkills.stream().filter(skill -> !resumeLower.contains(skill)).toList();

                    int score = (int) (((double) matched.size() / requiredSkills.size()) * 100);
                    String status = (score >= 40) ? "shortlisted" : "rejected";
                    String reason;
                    if (matched.isEmpty()) {
                        reason = "❌ Rejected. Resume lacks all required skills.";
                    } else if (score < 25) {
                        reason = "❌ Rejected. Very few matching skills.";
                    } else if ("shortlisted".equals(status)) {
                        reason = "✅ Shortlisted! Match Score: " + score + "%";
                    } else {
                        reason = "❌ Rejected. Missing key skills: " + String.join(", ", missing);
                    }

                    Application app = new Application();
                    app.setName(name);
                    app.setEmail(email);
                    app.setJobRole(jobRoleLower);
                    app.setMatchedSkills(String.join(",", matched));
                    app.setMissingSkills(String.join(",", missing));
                    app.setMatchScore(score);
                    app.setStatus(status);
                    app.setReason(reason);
                    app.setCreatedAt(LocalDateTime.now());

                    return repository.save(app);
                });
    }
}