package com.instashortlist.backend.scheduler;

import com.instashortlist.backend.service.Auth0TokenService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Component
public class LinkedInJob implements Job {

    @Autowired
    private Auth0TokenService tokenService;

    private final WebClient webClient = WebClient.create();

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("✅ Quartz Job triggered at: " + LocalDateTime.now());

        tokenService.getAccessToken()
                .flatMap(token -> webClient.get()
                        .uri("https://httpbin.org/bearer") // Replace with your protected endpoint
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(String.class))
                .subscribe(
                        res -> System.out.println("✅ API Response: " + res),
                        err -> System.err.println("❌ Job Error: " + err.getMessage())
                );
    }
}
