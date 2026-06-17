package com.rithi.idcard.model;

public class ProfileBuilder {

    public static Profile createDefaultProfile() {
        return Profile.builder()
                .fullName("Default User")
                .email("user@example.com")
                .phone("000000000")
                .department("IT")
                .profileType(ProfileType.USER)
                .build();
    }
}