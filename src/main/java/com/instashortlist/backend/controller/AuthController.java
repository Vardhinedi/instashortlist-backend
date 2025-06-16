package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.User;
import com.instashortlist.backend.repository.UserRepository;
import com.instashortlist.backend.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getUsername());
                        Map<String, Object> response = new HashMap<>();
                        response.put("message", "✅ Login successful");
                        response.put("username", user.getUsername());
                        response.put("role", user.getRole());
                        response.put("token", token);
                        return Mono.just(ResponseEntity.ok(response));
                    } else {
                        Map<String, Object> error = new HashMap<>();
                        error.put("error", "❌ Invalid credentials");
                        return Mono.just(ResponseEntity.status(401).body(error));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(401).body(Map.<String, Object>of(
                        "error", "❌ User not found"
                ))));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, Object>>> logout() {
        return Mono.just(ResponseEntity.ok(Map.<String, Object>of("message", "✅ Logged out")));
    }

    @GetMapping("/profile")
    public Mono<ResponseEntity<Map<String, Object>>> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);
                return userRepository.findByUsername(username)
                        .map(user -> ResponseEntity.ok(Map.<String, Object>of(
                                "username", user.getUsername(),
                                "email", user.getEmail(),
                                "role", user.getRole()
                        )))
                        .switchIfEmpty(Mono.just(ResponseEntity.status(404).body(Map.<String, Object>of(
                                "error", "❌ User not found"
                        ))));
            }
        }
        return Mono.just(ResponseEntity.status(401).body(Map.<String, Object>of(
                "error", "❌ Invalid or missing token"
        )));
    }
}
