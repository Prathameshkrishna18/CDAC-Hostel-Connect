package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.PhotoResponse;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.HostelPhoto;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.HostelPhotoRepository;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class HostelPhotoService {

    private final HostelPhotoRepository photoRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private String uploadDirectory;

    public HostelPhotoService(
            HostelPhotoRepository photoRepository,
            HostelRepository hostelRepository,
            UserRepository userRepository) {

        this.photoRepository = photoRepository;
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }

    public PhotoResponse uploadPhoto(
            Long hostelId,
            MultipartFile file,
            String description,
            String ownerEmail) throws IOException {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() ->
                        new RuntimeException("Hostel not found"));

        // Verify ownership
        if (!hostel.getOwner().getId()
                .equals(owner.getId())) {

            throw new RuntimeException(
                    "You are not authorized to modify this hostel"
            );
        }

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "Please select an image"
            );
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                 !contentType.equals("image/png") &&
                 !contentType.equals("image/webp"))) {

            throw new RuntimeException(
                    "Only JPG, PNG and WEBP images are allowed"
            );
        }

        // Create hostel-specific folder
        Path hostelDirectory = Paths.get(
                uploadDirectory,
                String.valueOf(hostelId)
        );

        Files.createDirectories(hostelDirectory);

        // Generate unique filename
        String extension = getExtension(
                file.getOriginalFilename()
        );

        String fileName =
                UUID.randomUUID() + extension;

        Path filePath =
                hostelDirectory.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Save URL/path in database
        HostelPhoto photo = new HostelPhoto();

        photo.setImageUrl(
                "/uploads/hostels/"
                        + hostelId
                        + "/"
                        + fileName
        );

        photo.setDescription(description);
        photo.setHostel(hostel);

        HostelPhoto saved =
                photoRepository.save(photo);

        return new PhotoResponse(
                saved.getId(),
                saved.getImageUrl(),
                saved.getDescription()
        );
    }

    public List<PhotoResponse> getPhotos(
            Long hostelId) {

        return photoRepository
                .findByHostelId(hostelId)
                .stream()
                .map(photo ->
                        new PhotoResponse(
                                photo.getId(),
                                photo.getImageUrl(),
                                photo.getDescription()
                        )
                )
                .toList();
    }

    public void deletePhoto(
            Long photoId,
            String ownerEmail) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Owner not found"
                        ));

        HostelPhoto photo =
                photoRepository.findById(photoId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Photo not found"
                                ));

        Hostel hostel = photo.getHostel();

        if (!hostel.getOwner().getId()
                .equals(owner.getId())) {

            throw new RuntimeException(
                    "You are not authorized"
            );
        }

        try {

            String imageUrl =
                    photo.getImageUrl();

            String relativePath =
                    imageUrl.substring(
                            imageUrl.indexOf(
                                    "/uploads/"
                            ) + 1
                    );

            Path path =
                    Paths.get(relativePath);

            Files.deleteIfExists(path);

        } catch (IOException ignored) {
            // Database record will still be removed.
        }

        photoRepository.delete(photo);
    }

    private String getExtension(
            String originalFileName) {

        if (originalFileName == null) {
            return ".jpg";
        }

        int index =
                originalFileName.lastIndexOf(".");

        if (index == -1) {
            return ".jpg";
        }

        return originalFileName.substring(index);
    }
}