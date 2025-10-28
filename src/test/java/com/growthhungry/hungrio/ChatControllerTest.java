package com.growthhungry.hungrio;

import com.growthhungry.hungrio.controller.ChatController;
import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.dto.ChatResponseDto;
import com.growthhungry.hungrio.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @SuppressWarnings("removal")
    @MockBean ChatService chatService;

    @Test
    void unauthenticated_returns401() throws Exception {
        mvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(new ChatRequestDto("hi"))))
           .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void authenticated_ok() throws Exception {
        when(chatService.processMessage(new ChatRequestDto("hi")))
            .thenReturn(new ChatResponseDto("ok","12:00:00"));

        mvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(new ChatRequestDto("hi"))))
           .andExpect(status().isOk());
    }
}
