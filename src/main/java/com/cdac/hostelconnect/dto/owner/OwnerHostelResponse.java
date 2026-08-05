package com.cdac.hostelconnect.dto.owner;

import com.cdac.hostelconnect.dto.FacilityResponse;
import com.cdac.hostelconnect.dto.FoodMenuResponse;
import com.cdac.hostelconnect.dto.RoomResponse;
import com.cdac.hostelconnect.entity.HostelStatus;

import java.util.List;

public class OwnerHostelResponse {

    private Long id;

    private String hostelName;

    private String description;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String contactNumber;

    private String email;

    private Double latitude;

    private Double longitude;

    private HostelStatus status;

    private String rejectionReason;

    private Long cdacCenterId;

    private String cdacCenterName;

    private List<FacilityResponse> facilities;

    private List<RoomResponse> rooms;

    private List<FoodMenuResponse> foodMenu;

    public OwnerHostelResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public HostelStatus getStatus() {
        return status;
    }

    public void setStatus(HostelStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Long getCdacCenterId() {
        return cdacCenterId;
    }

    public void setCdacCenterId(Long cdacCenterId) {
        this.cdacCenterId = cdacCenterId;
    }

    public String getCdacCenterName() {
        return cdacCenterName;
    }

    public void setCdacCenterName(String cdacCenterName) {
        this.cdacCenterName = cdacCenterName;
    }

    public List<FacilityResponse> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<FacilityResponse> facilities) {
        this.facilities = facilities;
    }

    public List<RoomResponse> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomResponse> rooms) {
        this.rooms = rooms;
    }

    public List<FoodMenuResponse> getFoodMenu() {
        return foodMenu;
    }

    public void setFoodMenu(List<FoodMenuResponse> foodMenu) {
        this.foodMenu = foodMenu;
    }
}