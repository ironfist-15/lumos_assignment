package com.chatApp.demo;

import java.time.Instant;

public class ConversationDTO {

    private String withUserId;
    private String userName;
    private String lastMessage;
    private Instant timestamp;

    public ConversationDTO(String withUserId, String userName, String lastMessage, Instant timestamp) {
        this.withUserId = withUserId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getWithUserId() {
        return withUserId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }
}
