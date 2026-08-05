package com.cdac.hostelconnect.dto;

public class StudentRegistrationRequest {

    private Long hostelId;

    private Long roomId;

    public StudentRegistrationRequest() {
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