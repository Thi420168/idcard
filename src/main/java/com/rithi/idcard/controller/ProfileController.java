package com.rithi.idcard.controller;

import com.rithi.idcard.model.Profile;
import com.rithi.idcard.service.PdfExportService;
import com.rithi.idcard.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final PdfExportService pdfExportService;

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }

    @PutMapping("/{id}")
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
        return profileService.updateProfile(id, profile);
    }

    @DeleteMapping("/{id}")
    public String deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return "Profile deleted successfully";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> exportProfilePdf(@PathVariable Long id) {
        Profile profile = profileService.getProfileById(id);
        byte[] pdf = pdfExportService.generateProfilePdf(id);
        String fileName = "id-card-" + profile.getRegistrationNumber() + ".pdf";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(pdf);
    }

    @GetMapping("/batch/pdf")
    public ResponseEntity<byte[]> exportBatchPdf() {
        byte[] pdf = pdfExportService.generateBatchPdf();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"id-cards-batch.pdf\"")
                .body(pdf);
    }
}
