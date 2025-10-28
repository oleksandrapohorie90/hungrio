package com.growthhungry.hungrio.service;

import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.dto.ChatResponseDto;

public interface ChatService {
    ChatResponseDto processMessage(ChatRequestDto request);
}
