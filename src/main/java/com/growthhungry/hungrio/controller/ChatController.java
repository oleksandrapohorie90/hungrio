package com.growthhungry.hungrio.controller;

import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.service.ChatService;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> body, Principal principal) {
        String message = body.get("message");

        // Use your existing method
        var dto = new ChatRequestDto(message);
        var response = chatService.processMessage(dto);

        return ResponseEntity.ok(Map.of(
            "response", response.getResponse(),
            "timestamp", response.getTimestamp()
        ));
    }
}


