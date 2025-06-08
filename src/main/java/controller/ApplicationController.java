package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.Application;
import com.instashortlist.backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
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
    public Mono<ResponseEntity<?>> apply(
            @RequestPart("resume") Mono<FilePart> resumeMono,
            @RequestPart("name") Mono<String> nameMono,
            @RequestPart("email") Mono<String> emailMono,
            @RequestPart("jobRole") Mono<String> jobRoleMono
    ) {
        return Mono.zip(resumeMono, nameMono, emailMono, jobRoleMono)
                .flatMap(tuple -> service.processApplication(
                        tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()
                ))
                .map(app -> ResponseEntity.ok(Map.of(
                        "status", app.getStatus(),
                        "score", app.getMatchScore(),
                        "matchedSkills", List.of(app.getMatchedSkills().split(",")),
                        "missingSkills", List.of(app.getMissingSkills().split(",")),
                        "reason", app.getReason()
                )));
    }
}
