package com.cdac.hostelconnect.dto;

public class OwnerDashboardResponse {

    private long totalHostels;
    private long approvedHostels;
    private long pendingHostels;
    private long rejectedHostels;
    
    private long totalRooms;
    private long totalBeds;
    private long availableBeds;

    private long totalRegistrations;
    private long pendingRegistrations;
    private long confirmedRegistrations;

    private double totalRevenue;

    private long unreadNotifications;
    private long openEnquiries;

    public OwnerDashboardResponse() {
    }

    public OwnerDashboardResponse(
            long totalHostels,
            long approvedHostels,
            long pendingHostels,
            long rejectedHostels) {

        this.totalHostels = totalHostels;
        this.approvedHostels = approvedHostels;
        this.pendingHostels = pendingHostels;
        this.rejectedHostels = rejectedHostels;
    }

    public long getTotalHostels() {
        return totalHostels;
    }

    public void setTotalHostels(long totalHostels) {
        this.totalHostels = totalHostels;
    }

    public long getApprovedHostels() {
        return approvedHostels;
    }

    public void setApprovedHostels(long approvedHostels) {
        this.approvedHostels = approvedHostels;
    }

    public long getPendingHostels() {
        return pendingHostels;
    }

    public void setPendingHostels(long pendingHostels) {
        this.pendingHostels = pendingHostels;
    }

    public long getRejectedHostels() {
        return rejectedHostels;
    }

    public void setRejectedHostels(long rejectedHostels) {
        this.rejectedHostels = rejectedHostels;
    }
}