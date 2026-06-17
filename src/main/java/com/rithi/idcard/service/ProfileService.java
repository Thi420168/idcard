package com.rithi.idcard.service;

import com.rithi.idcard.model.Profile;
import com.rithi.idcard.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private static final Pattern REGISTRATION_SUFFIX = Pattern.compile(".*-(\\d{3})$");

    private final ProfileRepository profileRepository;

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Profile getProfileById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile createProfile(Profile profile) {
        if (profile.getUuid() == null || profile.getUuid().isBlank()) {
            profile.setUuid(UUID.randomUUID().toString());
        }

        profile.setRegistrationNumber(generateRegistrationNumber(profile.getDepartment()));

        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long id, Profile newProfile) {
        Profile profile = getProfileById(id);

        profile.setType(newProfile.getType());
        profile.setFullName(newProfile.getFullName());
        profile.setDepartment(newProfile.getDepartment());
        profile.setTitle(newProfile.getTitle());
        profile.setEmail(newProfile.getEmail());
        profile.setPhone(newProfile.getPhone());
        profile.setBloodGroup(newProfile.getBloodGroup());
        profile.setDateOfBirth(newProfile.getDateOfBirth());
        profile.setIssueDate(newProfile.getIssueDate());
        profile.setExpiryDate(newProfile.getExpiryDate());
        profile.setTemplate(newProfile.getTemplate());
        profile.setBarcodeType(newProfile.getBarcodeType());

        return profileRepository.save(profile);
    }

    public Profile updatePhoto(Long id, String fileName, String contentType) {
        Profile profile = getProfileById(id);
        profile.setPhotoFileName(fileName);
        profile.setPhotoContentType(contentType);
        return profileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    private String generateRegistrationNumber(String department) {
        String dept = normalizeDepartmentCode(department);
        String prefix = Year.now() + "-" + dept + "-";
        int next = profileRepository.findTopByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(prefix)
                .map(Profile::getRegistrationNumber)
                .map(this::readRegistrationSuffix)
                .orElse(0) + 1;

        String registrationNumber;
        do {
            registrationNumber = prefix + String.format("%03d", next++);
        } while (profileRepository.existsByRegistrationNumber(registrationNumber));

        return registrationNumber;
    }

    private String normalizeDepartmentCode(String department) {
        if (department == null || department.isBlank()) {
            return "GEN";
        }
        String normalized = department.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        if (normalized.isBlank()) {
            return "GEN";
        }
        return normalized.substring(0, Math.min(3, normalized.length()));
    }

    private int readRegistrationSuffix(String registrationNumber) {
        Matcher matcher = REGISTRATION_SUFFIX.matcher(registrationNumber);
        if (!matcher.matches()) {
            return 0;
        }
        return Integer.parseInt(matcher.group(1));
    }
}
