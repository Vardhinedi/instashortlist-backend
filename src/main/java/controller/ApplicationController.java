package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationService service;

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("✅ Reactive controller is working!");
    }

    @PostMapping(value = "/apply", consumes = "multipart/form-data")
    public Mono<ResponseEntity<Map<String, Object>>> apply(
            @RequestPart("resume") Mono<FilePart> resumeMono,
            @RequestPart("name") Mono<String> nameMono,
            @RequestPart("email") Mono<String> emailMono,
            @RequestPart("jobRole") Mono<String> jobRoleMono
    ) {
        return Mono.zip(resumeMono, nameMono, emailMono, jobRoleMono)
                .flatMap(tuple -> {
                    FilePart resume = tuple.getT1();
                    String name = tuple.getT2();
                    String email = tuple.getT3();
                    String jobRole = tuple.getT4().toLowerCase().trim();

                    return service.saveResumeFile(resume)
                            .flatMap(savedPath -> {
                                Application app = new Application();
                                app.setName(name);
                                app.setEmail(email);
                                app.setJobRole(jobRole);
                                app.setResumePath(savedPath);
                                app.setStatus("pending");
                                app.setCreatedAt(LocalDateTime.now());

                                return service.save(app);
                            });
                })
                .map(saved -> ResponseEntity.ok(Map.of(
                        "status", "pending",
                        "message", "🕒 Application received. Will be processed shortly."
                )));
    }
}
