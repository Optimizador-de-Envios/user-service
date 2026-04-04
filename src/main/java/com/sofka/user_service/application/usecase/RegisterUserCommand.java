package com.sofka.user_service.application.usecase;

public record RegisterUserCommand(String name, String email, String password) {
}