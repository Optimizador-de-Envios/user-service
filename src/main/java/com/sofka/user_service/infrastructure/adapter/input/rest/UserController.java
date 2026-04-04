package com.sofka.user_service.infrastructure.adapter.input.rest;

import com.sofka.user_service.application.port.input.RegisterUserUseCase;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.RegisterUserRequest;
import com.sofka.user_service.infrastructure.adapter.input.rest.dto.UserResponse;
import com.sofka.user_service.infrastructure.mapper.UserRestMapper;
import jakarta.validation.Valid;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final RegisterUserUseCase registerUserUseCase;
	private final UserRestMapper userRestMapper;

	public UserController(RegisterUserUseCase registerUserUseCase, UserRestMapper userRestMapper) {
		this.registerUserUseCase = Objects.requireNonNull(registerUserUseCase, "registerUserUseCase must not be null");
		this.userRestMapper = Objects.requireNonNull(userRestMapper, "userRestMapper must not be null");
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
		User user = registerUserUseCase.register(userRestMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(userRestMapper.toResponse(user));
	}
}