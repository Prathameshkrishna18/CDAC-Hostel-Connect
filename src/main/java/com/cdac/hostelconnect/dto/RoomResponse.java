package com.cdac.hostelconnect.dto;

import com.cdac.hostelconnect.entity.SharingType;

public class RoomResponse {

    private Long id;
    private SharingType sharingType;
    private Double rent;
    private Integer totalBeds;
    private Integer availableBeds;
    private String description;

    public RoomResponse(
            Long id,
            SharingType sharingType,
            Double rent,
            Integer totalBeds,
            Integer availableBeds,
            String description) {

        this.id = id;
        this.sharingType = sharingType;
        this.rent = rent;
        this.totalBeds = totalBeds;
        this.availableBeds = availableBeds;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public SharingType getSharingType() {
        return sharingType;
    }

    public Double getRent() {
        return rent;
    }

    public Integer getTotalBeds() {
        return totalBeds;
    }

    public Integer getAvailableBeds() {
        return availableBeds;
    }

    public String getDescription() {
        return description;
    }
}