package com.instashortlist.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class Auth0TokenService {

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.token-uri}")
    private String tokenUri;

    private final WebClient webClient = WebClient.create();

    public Mono<String> getAccessToken() {
        return webClient.post()
                .uri(tokenUri)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "audience", audience
                ))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("❌ Auth0 Error Response: " + errorBody);
                                    return Mono.error(new RuntimeException("Auth0 token request failed: " + errorBody));
                                })
                )
                .bodyToMono(Map.class)
                .map(body -> (String) body.get("access_token"))
                .doOnError(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException we = (WebClientResponseException) e;
                        System.err.println("❌ WebClientResponseException: " + we.getResponseBodyAsString());
                    } else {
                        System.err.println("❌ Unexpected Error: " + e.getMessage());
                    }
                });
    }
}
