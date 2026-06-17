package com.rithi.idcard.controller;

import com.rithi.idcard.model.Profile;
import com.rithi.idcard.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FileController {

    private static final long MAX_PHOTO_SIZE = 2L * 1024L * 1024L;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE);
    private static final Path UPLOAD_DIR = Paths.get("uploads").toAbsolutePath().normalize();

    private final ProfileService profileService;

    @PostMapping("/api/profiles/{id}/photo")
    public Profile uploadPhoto(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) {
        if (photo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo is required");
        }
        String contentType = photo.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG and PNG images are allowed");
        }
        if (photo.getSize() > MAX_PHOTO_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Photo must be 2MB or smaller");
        }

        try {
            Files.createDirectories(UPLOAD_DIR);
            String extension = MediaType.IMAGE_PNG_VALUE.equals(contentType) ? ".png" : ".jpg";
            String fileName = UUID.randomUUID() + extension;
            Path target = UPLOAD_DIR.resolve(fileName).normalize();
            Files.copy(photo.getInputStream(), target);
            return profileService.updatePhoto(id, fileName, contentType);
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to save photo", exception);
        }
    }

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String fileName) {
        try {
            Path file = UPLOAD_DIR.resolve(fileName).normalize();
            if (!file.startsWith(UPLOAD_DIR) || !Files.exists(file) || !Files.isRegularFile(file)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");
            }

            Resource resource = new UrlResource(file.toUri());
            String mediaType = fileName.toLowerCase(Locale.ROOT).endsWith(".png")
                    ? MediaType.IMAGE_PNG_VALUE
                    : MediaType.IMAGE_JPEG_VALUE;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400")
                    .body(resource);
        } catch (MalformedURLException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found", exception);
        }
    }
}
