package com.sofka.user_service.infrastructure.mapper;

import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.RegisterUserRequest;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper {

	public RegisterUserCommand toCommand(RegisterUserRequest request) {
		return new RegisterUserCommand(request.name(), request.email(), request.password());
	}

	public UserResponse toResponse(User user) {
		return new UserResponse(user.id(), user.name().value(), user.email().value(), user.createdAt());
	}
}