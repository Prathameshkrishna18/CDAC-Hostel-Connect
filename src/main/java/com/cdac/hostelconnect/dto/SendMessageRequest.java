package com.cdac.hostelconnect.dto;

public class SendMessageRequest {

    private Long hostelId;

    private String message;

    public SendMessageRequest() {
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}