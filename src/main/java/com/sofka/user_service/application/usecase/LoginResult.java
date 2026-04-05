package com.sofka.user_service.application.usecase;

public record LoginResult(String accessToken, String tokenType, long expiresIn, LoginResultUser user) {
}