package com.example.chatapp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class ChatMessage {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    @NotBlank(message = "receiver cannot be null")
    private String receiver;


    private String content;
    private String timestamp;

    @Column(nullable = false)
    private String roomId;

    public ChatMessage() {}

    public ChatMessage(String sender, String receiver, String content, String timestamp, String roomId) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
