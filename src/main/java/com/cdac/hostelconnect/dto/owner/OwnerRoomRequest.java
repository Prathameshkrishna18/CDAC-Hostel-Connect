package com.cdac.hostelconnect.dto.owner;

import com.cdac.hostelconnect.entity.SharingType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OwnerRoomRequest {

    @NotNull
    private SharingType sharingType;

    @NotNull
    @Min(0)
    private Double rent;

    @NotNull
    @Min(1)
    private Integer totalBeds;

    @NotNull
    @Min(0)
    private Integer availableBeds;

    private String description;

    public OwnerRoomRequest() {
    }

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