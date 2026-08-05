package com.cdac.hostelconnect.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;

    public ChatMessageResponse() {
    }

    public ChatMessageResponse(
            Long id,
            Long conversationId,
            Long senderId,
            String senderName,
            Long receiverId,
            String message,
            boolean read,
            LocalDateTime createdAt) {

        this.id = id;
        this.conversationId = conversationId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}