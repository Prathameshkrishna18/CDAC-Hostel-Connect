package com.cdac.hostelconnect.dto;

public class PaymentOrderResponse {

    private Long registrationId;
    private String orderId;
    private Double amount;
    private String currency;
    private String hostelName;
    private String roomType;

    public PaymentOrderResponse() {
    }

    public PaymentOrderResponse(
            Long registrationId,
            String orderId,
            Double amount,
            String currency,
            String hostelName,
            String roomType) {

        this.registrationId = registrationId;
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.hostelName = hostelName;
        this.roomType = roomType;
    }

    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}