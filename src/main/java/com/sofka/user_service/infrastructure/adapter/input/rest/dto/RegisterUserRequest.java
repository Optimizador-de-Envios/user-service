package com.sofka.user_service.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
	@NotBlank(message = "Name is required") String name,
	@NotBlank(message = "Email is required") @Email(message = "Email must be a valid email") String email,
	@NotBlank(message = "Password is required") @Size(min = 8, message = "Password must contain at least 8 characters") String password) {
}