package com.cdac.hostelconnect.dto;

public class PhotoResponse {

    private Long id;
    private String imageUrl;
    private String description;

    public PhotoResponse(
            Long id,
            String imageUrl,
            String description) {

        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}