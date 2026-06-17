package com.rithi.idcard.repository;

import com.rithi.idcard.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    boolean existsByCode(String code);
}
