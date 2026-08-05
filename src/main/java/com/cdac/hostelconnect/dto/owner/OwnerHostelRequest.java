package com.cdac.hostelconnect.dto.owner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OwnerHostelRequest {

    @NotBlank
    private String hostelName;

    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    private String pincode;

    private String contactNumber;

    private String email;

    private Double latitude;

    private Double longitude;

    @NotNull
    private Long cdacCenterId;

    private List<Long> facilityIds;

    public OwnerHostelRequest() {
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getCdacCenterId() {
        return cdacCenterId;
    }

    public void setCdacCenterId(Long cdacCenterId) {
        this.cdacCenterId = cdacCenterId;
    }

    public List<Long> getFacilityIds() {
        return facilityIds;
    }

    public void setFacilityIds(List<Long> facilityIds) {
        this.facilityIds = facilityIds;
    }
}