package com.sofka.user_service.application.usecase;

public record LoginCommand(String email, String password) {
}