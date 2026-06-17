package com.rithi.idcard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String templateName;

    @Column(columnDefinition = "TEXT")
    private String htmlContent;

    @Enumerated(EnumType.STRING)
    private BarcodeType barcodeType;
}