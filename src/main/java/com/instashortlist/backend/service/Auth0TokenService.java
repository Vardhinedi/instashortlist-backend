package com.instashortlist.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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
                .bodyValue(Map.of(
                        "grant_type", "client_credentials",
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "audience", audience
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(body -> (String) body.get("access_token"));
    }
}
