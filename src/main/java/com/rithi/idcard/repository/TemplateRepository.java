package com.rithi.idcard.repository;

import com.rithi.idcard.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByTemplateName(String templateName);

    boolean existsByTemplateName(String templateName);
}