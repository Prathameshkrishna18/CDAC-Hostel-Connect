package com.cdac.hostelconnect.dto;

import com.cdac.hostelconnect.entity.SharingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RoomRequest {

    @NotNull(message = "Sharing type is required")
    private SharingType sharingType;

    @NotNull(message = "Rent is required")
    @Min(value = 0, message = "Rent cannot be negative")
    private Double rent;

    @NotNull(message = "Total beds are required")
    @Min(value = 1, message = "Total beds must be at least 1")
    private Integer totalBeds;

    @NotNull(message = "Available beds are required")
    @Min(value = 0, message = "Available beds cannot be negative")
    private Integer availableBeds;

    private String description;


    public SharingType getSharingType() {
        return sharingType;
    }

    public void setSharingType(SharingType sharingType) {
        this.sharingType = sharingType;
    }


    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }


    public Integer getTotalBeds() {
        return totalBeds;
    }

    public void setTotalBeds(Integer totalBeds) {
        this.totalBeds = totalBeds;
    }


    public Integer getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(Integer availableBeds) {
        this.availableBeds = availableBeds;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}