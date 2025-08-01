package com.instashortlist.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // ✅ Allow only your trusted frontend origins
        config.addAllowedOriginPattern("http://localhost:4200"); // Local dev
        config.addAllowedOriginPattern("http://172.30.12.158:4200"); // VM IP
        config.addAllowedOriginPattern("https://insta-shortlist-angular.vercel.app"); // Vercel prod
        config.addAllowedOriginPattern("https://*.ngrok-free.app"); // Ngrok dynamic

        // ✅ Allow all headers and methods for frontend compatibility
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // ✅ Optional: Allow frontend to read auth header
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
