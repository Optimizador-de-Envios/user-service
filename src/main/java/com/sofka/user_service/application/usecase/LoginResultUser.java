package com.sofka.user_service.application.usecase;

import java.util.UUID;

public record LoginResultUser(UUID id, String name, String email) {
}