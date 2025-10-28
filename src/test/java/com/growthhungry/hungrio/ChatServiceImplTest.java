package com.growthhungry.hungrio;

import com.growthhungry.hungrio.dto.ChatRequestDto;
import com.growthhungry.hungrio.service.ChatServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatServiceImplTest {

    @Test
    void echoes_with_time() {
        var svc = new ChatServiceImpl();
        var res = svc.processMessage(new ChatRequestDto("hello"));
        assertThat(res.getResponse()).contains("Server received: hello");
        assertThat(res.getTimestamp()).isNotBlank();
    }
}
