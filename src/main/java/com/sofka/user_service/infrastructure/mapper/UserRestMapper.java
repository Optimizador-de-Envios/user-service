package com.sofka.user_service.infrastructure.mapper;

import com.sofka.user_service.application.usecase.LoginCommand;
import com.sofka.user_service.application.usecase.LoginResult;
import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.LoginRequest;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.LoginResponse;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.LoginUserResponse;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.RegisterUserRequest;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper {

	public LoginCommand toCommand(LoginRequest request) {
		return new LoginCommand(request.email(), request.password());
	}

	public RegisterUserCommand toCommand(RegisterUserRequest request) {
		return new RegisterUserCommand(request.name(), request.email(), request.password());
	}

	public LoginResponse toResponse(LoginResult result) {
		return new LoginResponse(result.accessToken(), result.tokenType(), result.expiresIn(), new LoginUserResponse(result.user().id(), result.user().name(), result.user().email()));
	}

	public UserResponse toResponse(User user) {
		return new UserResponse(user.id(), user.name().value(), user.email().value(), user.createdAt());
	}
}