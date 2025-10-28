package com.growthhungry.hungrio.controller;

import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.dto.ChatResponseDto;
import com.growthhungry.hungrio.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto req) {
        return ResponseEntity.ok(chatService.processMessage(req));
    }
}
