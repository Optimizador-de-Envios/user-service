package com.sofka.user_service.infrastructure.adapter.input.rest.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresIn, LoginUserResponse user) {
}