package com.instashortlist.backend.scheduler;

import com.instashortlist.backend.config.Auth0TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class DummyScheduler {

    @Autowired
    private Auth0TokenService tokenService;

    private final WebClient webClient = WebClient.create(); // Optional: can also be a Bean

    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void fetchData() {
        System.out.println("✅ Scheduled job started at: " + java.time.LocalDateTime.now());

        tokenService.getAccessToken().flatMap(token -> {
            return webClient.get()
                    .uri("https://httpbin.org/bearer") // Replace with your real endpoint
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class);
        }).subscribe(
                response -> System.out.println("✅ API response: " + response),
                error -> System.err.println("❌ Error during API call: " + error.getMessage())
        );
    }
}
