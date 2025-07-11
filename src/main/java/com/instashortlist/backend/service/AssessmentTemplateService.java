package com.instashortlist.backend.service;

import com.instashortlist.backend.model.AssessmentTemplate;
import com.instashortlist.backend.repository.AssessmentTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AssessmentTemplateService {

    @Autowired
    private AssessmentTemplateRepository templateRepository;

    // Fetch all templates (optional)
    public Flux<AssessmentTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

    // Fetch templates by role
    public Flux<AssessmentTemplate> getTemplatesByRole(String role) {
        return templateRepository.findByRoleOrderByStepOrderAsc(role);
    }
}
