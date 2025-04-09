package com.example.chatapp.controller;

import com.example.chatapp.entity.Message;
import com.example.chatapp.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Validated
@CrossOrigin(origins = "*") // If you're testing with frontend
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Save Message
    @PostMapping("/send")
    public Message sendMessage(@Valid @RequestBody Message message) {
        return messageService.saveMessage(message);
    }

    // Get Chat History
    @GetMapping("/history/room/{roomId}")
    public List<Message> getChatHistory(@PathVariable String roomId) {
        return messageService.getMessagesByRoom(roomId);
    }

    @GetMapping("/history/users/{sender}/{receiver}")
    public ResponseEntity<?> getChatHistoryBetweenUsers(
            @PathVariable String sender,
            @PathVariable String receiver,
            Principal principal) {

        System.out.println("👤 Authenticated user: " + principal.getName());
        System.out.println("📨 Request: sender = " + sender + ", receiver = " + receiver);

        if (!principal.getName().equals(sender) && !principal.getName().equals(receiver)) {
            return ResponseEntity.status(403).body("❌ Forbidden: User not authorized for this chat");
        }

        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(sender, receiver));
    }

}
