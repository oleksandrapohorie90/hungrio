package com.growthhungry.hungrio.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.dto.ChatResponseDto;

// @Service
// public class ChatServiceImpl implements ChatService {

//     @Override
//     public ChatResponseDto processMessage(ChatRequestDto request) {
//         // TODO: Implement actual AI service integration here
//         // This is a placeholder implementation
//         String response = "Echo: " + request.getMessage();
//         String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
//         return new ChatResponseDto(response, timestamp);
//     }
// }

@Service
public class ChatServiceImpl implements ChatService {

    @Override
    public ChatResponseDto processMessage(ChatRequestDto request) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new ChatResponseDto("Server received: " + request.getMessage(), time);
    }
}
