package com.cdac.hostelconnect.dto;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequest {

    @NotNull
    private Long hostelId;

    @NotNull
    private Long roomId;

    public RegistrationRequest() {
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}