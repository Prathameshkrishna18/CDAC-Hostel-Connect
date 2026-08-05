package com.cdac.hostelconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class RejectHostelRequest {

    @NotBlank(message = "Rejection reason is required")
    private String reason;

    public RejectHostelRequest() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}