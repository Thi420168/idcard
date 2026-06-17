package com.rithi.idcard.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String photoPath;

    @Enumerated(EnumType.STRING)
    private ProfileType profileType;
}