package com.instashortlist.backend.controller;

import com.instashortlist.backend.model.AssessmentTemplate;
import com.instashortlist.backend.service.AssessmentTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/assessment-templates")
@CrossOrigin(origins = "*")  // You can restrict this for production
public class AssessmentTemplateController {

    @Autowired
    private AssessmentTemplateService templateService;

    // 🔹 Get all templates (optional)
    @GetMapping
    public Flux<AssessmentTemplate> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    // 🔹 Get templates by role
    @GetMapping("/role/{role}")
    public Flux<AssessmentTemplate> getTemplatesByRole(@PathVariable String role) {
        return templateService.getTemplatesByRole(role);
    }
}
