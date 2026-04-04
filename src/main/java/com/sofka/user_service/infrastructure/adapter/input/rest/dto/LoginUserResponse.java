package com.sofka.user_service.infrastructure.adapter.input.rest.dto;

import java.util.UUID;

public record LoginUserResponse(UUID id, String name, String email) {
}