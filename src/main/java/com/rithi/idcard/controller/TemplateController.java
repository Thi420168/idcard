package com.rithi.idcard.controller;

import com.rithi.idcard.model.Template;
import com.rithi.idcard.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public List<Template> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @PostMapping
    public Template createTemplate(@RequestBody Template template) {
        return templateService.createTemplate(template);
    }

    @GetMapping("/{id}")
    public Template getTemplateById(@PathVariable Long id) {
        return templateService.getTemplateById(id);
    }

    @PutMapping("/{id}")
    public Template updateTemplate(@PathVariable Long id, @RequestBody Template template) {
        return templateService.updateTemplate(id, template);
    }

    @DeleteMapping("/{id}")
    public String deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return "Template deleted successfully";
    }
}
