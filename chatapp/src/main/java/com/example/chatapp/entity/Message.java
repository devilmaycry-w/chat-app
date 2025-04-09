package com.example.chatapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "sender cannot be null")
    @Column(nullable = false)
    private String sender;

    private String content;

    @Column(nullable = false)
    private String roomId;

    @NotBlank(message = "receiver cannot be null")
    @Column(nullable = false)
    private String receiver;

    private LocalDateTime timestamp;

    // Constructors
    public Message() {
        this.timestamp = LocalDateTime.now();
    }

    public Message(String sender, String receiver, String content, String roomId) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.roomId = roomId;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters

}
