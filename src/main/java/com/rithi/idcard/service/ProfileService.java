package com.rithi.idcard.service;

import com.rithi.idcard.model.Profile;
import com.rithi.idcard.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile createProfile(Profile profile) {
        profile.setRegistrationNumber(generateRegistrationNumber());
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, Profile newProfile) {
        Profile profile = getProfileById(id);

        profile.setFullName(newProfile.getFullName());
        profile.setEmail(newProfile.getEmail());
        profile.setPhone(newProfile.getPhone());
        profile.setDepartment(newProfile.getDepartment());

        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    private String generateRegistrationNumber() {
        return Year.now() + "-ID-" + UUID.randomUUID().toString().substring(0, 8);
    }
}