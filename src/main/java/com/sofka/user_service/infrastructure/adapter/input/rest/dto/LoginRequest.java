package com.sofka.user_service.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
	@NotBlank(message = "Email is invalid") @Email(message = "Email is invalid") String email,
	@NotBlank(message = "Password is required") String password) {
}