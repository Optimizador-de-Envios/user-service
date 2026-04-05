package com.sofka.user_service.infrastructure.adapter.input.rest;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.sofka.user_service.application.port.input.LoginUseCase;
import com.sofka.user_service.application.port.input.RegisterUserUseCase;
import com.sofka.user_service.application.usecase.LoginCommand;
import com.sofka.user_service.application.usecase.LoginResult;
import com.sofka.user_service.application.usecase.LoginResultUser;
import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.config.exception.GlobalExceptionHandler;
import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.exception.InvalidCredentialsException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import com.sofka.user_service.infrastructure.mapper.UserRestMapper;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private RegisterUserUseCase registerUserUseCase;

	@Mock
	private LoginUseCase loginUseCase;

	private final UserRestMapper userRestMapper = new UserRestMapper();
	private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

	private MockMvc createMockMvc() {
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
		validatorFactoryBean.afterPropertiesSet();
		return MockMvcBuilders.standaloneSetup(new UserController(registerUserUseCase, loginUseCase, userRestMapper))
			.setControllerAdvice(globalExceptionHandler)
			.setValidator(validatorFactoryBean)
			.build();
	}

	@Test
	void shouldLoginAndReturnTokenResponse() throws Exception {
		MockMvc mockMvc = createMockMvc();
		LoginResult result = new LoginResult("jwt-token", "Bearer", 86400L, new LoginResultUser(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), "Juan Perez", "juan@example.com"));
		when(loginUseCase.login(any(LoginCommand.class))).thenReturn(result);

		mockMvc.perform(post("/api/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"juan@example.com\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.accessToken").value("jwt-token"))
			.andExpect(jsonPath("$.tokenType").value("Bearer"))
			.andExpect(jsonPath("$.expiresIn").value(86400))
			.andExpect(jsonPath("$.user.id").value("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"))
			.andExpect(jsonPath("$.user.name").value("Juan Perez"))
			.andExpect(jsonPath("$.user.email").value("juan@example.com"));
	}

	@Test
	void shouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
		MockMvc mockMvc = createMockMvc();
		when(loginUseCase.login(any(LoginCommand.class))).thenThrow(new InvalidCredentialsException("Invalid credentials"));

		mockMvc.perform(post("/api/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"juan@example.com\",\"password\":\"wrong-password\"}"))
			.andExpect(status().isUnauthorized())
			.andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"))
			.andExpect(jsonPath("$.message").value("The email or password is incorrect"))
			.andExpect(jsonPath("$.errors[0]").value("Invalid credentials"));
	}

	@Test
	void shouldRegisterUserAndReturnCreatedResponse() throws Exception {
		MockMvc mockMvc = createMockMvc();
		User user = User.register(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));
		when(registerUserUseCase.register(any(RegisterUserCommand.class))).thenReturn(user);

		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Juan Perez\",\"email\":\"juan@example.com\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"))
			.andExpect(jsonPath("$.name").value("Juan Perez"))
			.andExpect(jsonPath("$.email").value("juan@example.com"))
			.andExpect(jsonPath("$.createdAt").value("2026-04-03T18:30:00Z"));
	}

	@Test
	void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {
		MockMvc mockMvc = createMockMvc();
		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Juan Perez\",\"email\":\"invalid-email\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.message").value("The request contains invalid data"))
			.andExpect(jsonPath("$.errors[0]").value("email: Email must be a valid email"));
	}

	@Test
	void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
		MockMvc mockMvc = createMockMvc();
		when(registerUserUseCase.register(any(RegisterUserCommand.class))).thenThrow(new DuplicateUserEmailException("Email is already in use"));

		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Juan Perez\",\"email\":\"juan@example.com\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"))
			.andExpect(jsonPath("$.message").value("Email is already in use"));
	}

	@Test
	void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
		MockMvc mockMvc = createMockMvc();
		mockMvc.perform(post("/api/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"juan@example.com\",\"password\":"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("INVALID_JSON"))
			.andExpect(jsonPath("$.message").value("The request body is not readable"));
	}
}