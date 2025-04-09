package com.example.chatapp.controller;

import com.example.chatapp.dto.ChatMessage;
import com.example.chatapp.entity.Message;
import com.example.chatapp.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    // Broadcast to all
    @MessageMapping("/sendMessage") // maps to /app/sendMessage
    @SendTo("/topic/messages") // messages broadcast to /topic/messages
    public ChatMessage broadcastMessage(ChatMessage message) {
        // Add server-side timestamp
        //String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        saveMessageToDB(message);
        message.setTimestamp(currentTimestamp());
        return message;
    }

    // private messaging
    @MessageMapping("/private-message") // maps to /app/private-message
    public void sendPrivateMessage(ChatMessage message) {
        message.setTimestamp(currentTimestamp());
        saveMessageToDB(message);

        // send only to the receivers queue
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(), "/queue/messages", message);
    }

    private void saveMessageToDB(ChatMessage chatMessage) {
        Message dbMessage = new Message();
        dbMessage.setContent(chatMessage.getContent());
        dbMessage.setRoomId(chatMessage.getRoomId());
        dbMessage.setSender(chatMessage.getSender());
        dbMessage.setReceiver(chatMessage.getReceiver());
        dbMessage.setTimestamp(LocalDateTime.now());

        messageService.saveMessage(dbMessage);
    }

    private String currentTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}