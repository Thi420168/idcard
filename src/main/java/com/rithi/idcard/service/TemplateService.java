package com.rithi.idcard.service;

import com.rithi.idcard.model.Template;
import com.rithi.idcard.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public List<Template> getAllTemplates() {
        return templateRepository.findAll();
    }

    public Template createTemplate(Template template) {
        return templateRepository.save(template);
    }

    public Template getTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
}