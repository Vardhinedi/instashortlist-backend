package com.instashortlist.backend.config;

import lombok.Data;
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

    public Mono<String> getAccessToken() {
        WebClient webClient = WebClient.create();

        return webClient.post()
                .uri(tokenUri)
                .bodyValue(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "audience", audience,
                        "grant_type", "client_credentials"
                ))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::getAccess_token);
    }

    @Data
    private static class TokenResponse {
        private String access_token;
        private String token_type;
        private int expires_in;
    }
}
