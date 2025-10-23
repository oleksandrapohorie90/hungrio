package com.growthhungry.hungrio.dto;

import java.time.Instant;

public record ChatResponse(
    String reply,
    Instant timestamp
) {}
