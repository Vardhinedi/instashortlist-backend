package com.instashortlist.backend.util;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ResumeMatcherClient {

    private final WebClient webClient;

    public ResumeMatcherClient() {
        this.webClient = WebClient.create("http://localhost:8000"); // FastAPI base URL
    }

    public Mono<Map<String, Object>> getMatchScore(String resumeText, String jobDescription) {
        return webClient.post()
                .uri("/match")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "resume_text", resumeText,
                        "job_description", jobDescription
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
