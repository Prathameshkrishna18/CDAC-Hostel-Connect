package com.cdac.hostelconnect.dto;

public class OwnerProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String contactNumber;

    public OwnerProfileResponse() {
    }

    public OwnerProfileResponse(
            Long id,
            String name,
            String email,
            String contactNumber) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}