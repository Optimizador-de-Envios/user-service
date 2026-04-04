package com.sofka.user_service.infrastructure.adapter.input.rest.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(UUID id, String name, String email, Instant createdAt) {
}