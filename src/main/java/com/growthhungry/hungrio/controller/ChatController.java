package com.growthhungry.hungrio.controller;

import com.growthhungry.hungrio.dto.ChatRequest;
import com.growthhungry.hungrio.dto.ChatResponse;
import com.growthhungry.hungrio.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest req) {
        String reply = chatService.sendMessage(req.message());
        return ResponseEntity.ok(new ChatResponse(reply, Instant.now()));
    }
}
