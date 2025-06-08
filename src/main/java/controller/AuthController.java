package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.User;
import com.instashortlist.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.just(ResponseEntity.ok(Map.of(
                                "message", "✅ Login successful",
                                "username", user.getUsername(),
                                "role", user.getRole()
                        )));
                    } else {
                        return Mono.just(ResponseEntity.status(401).<Object>body(Map.of(
                                "error", "❌ Invalid credentials"
                        )));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(401).<Object>body(Map.of(
                        "error", "❌ User not found"
                ))));
    }
}
