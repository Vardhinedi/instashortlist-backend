package com.instashortlist.backend.jobs;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.repository.ApplicationRepository;
import com.instashortlist.backend.utils.ResumeTextExtractor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationAutoProcessorJob implements Job {

    @Autowired
    private ApplicationRepository repository;

    private static final Map<String, List<String>> expectedSkills = Map.of(
            "java developer", List.of("java", "spring", "hibernate", "rest", "mysql"),
            "frontend developer", List.of("html", "css", "javascript", "react", "tailwind"),
            "data analyst", List.of("sql", "excel", "python", "powerbi", "tableau")
    );

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("🔄 Running ApplicationAutoProcessorJob...");

        repository.findAll()
                .filter(app -> "pending".equalsIgnoreCase(app.getStatus()))
                .map(app -> {
                    String roleKey = expectedSkills.keySet().stream()
                            .filter(key -> app.getJobRole().toLowerCase().contains(key))
                            .findFirst().orElse(null);

                    if (roleKey == null) {
                        app.setStatus("rejected");
                        app.setReason("❌ Unsupported job role.");
                        return app;
                    }

                    List<String> requiredSkills = expectedSkills.get(roleKey);
                    File resumeFile = new File(app.getResumePath());

                    String resumeText = ResumeTextExtractor.extractText(resumeFile).toLowerCase();
                    List<String> matched = requiredSkills.stream().filter(resumeText::contains).toList();
                    List<String> missing = requiredSkills.stream().filter(skill -> !resumeText.contains(skill)).toList();

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

                    app.setMatchedSkills(String.join(",", matched));
                    app.setMissingSkills(String.join(",", missing));
                    app.setMatchScore(score);
                    app.setStatus(status);
                    app.setReason(reason);
                    app.setCreatedAt(LocalDateTime.now());

                    return app;
                })
                .flatMap(repository::save)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(saved -> System.out.println("✔ Processed application: " + saved.getId()));
    }
}
