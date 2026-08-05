package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.PhotoResponse;
import com.cdac.hostelconnect.service.HostelPhotoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/owner/hostels")
public class HostelPhotoController {

    private final HostelPhotoService photoService;

    public HostelPhotoController(
            HostelPhotoService photoService) {

        this.photoService = photoService;
    }

    @PostMapping(
            value = "/{hostelId}/photos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<PhotoResponse> uploadPhoto(
            @PathVariable Long hostelId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(
                    value = "description",
                    required = false
            ) String description,
            Authentication authentication)
            throws IOException {

        return ResponseEntity.ok(
                photoService.uploadPhoto(
                        hostelId,
                        file,
                        description,
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{hostelId}/photos")
    public ResponseEntity<List<PhotoResponse>>
    getPhotos(
            @PathVariable Long hostelId) {

        return ResponseEntity.ok(
                photoService.getPhotos(hostelId)
        );
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<String> deletePhoto(
            @PathVariable Long photoId,
            Authentication authentication) {

        photoService.deletePhoto(
                photoId,
                authentication.getName()
        );

        return ResponseEntity.ok(
                "Photo deleted successfully"
        );
    }
}