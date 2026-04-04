package com.sofka.user_service.infrastructure.adapter.input.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sofka.user_service.application.port.input.RegisterUserUseCase;
import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.config.exception.GlobalExceptionHandler;
import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import com.sofka.user_service.infrastructure.mapper.UserRestMapper;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

	@Mock
	private RegisterUserUseCase registerUserUseCase;

	private MockMvc mockMvc;

	private UserController userController;
	private final UserRestMapper userRestMapper = new UserRestMapper();
	private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

	@BeforeEach
	void setUp() {
		LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
		validatorFactoryBean.afterPropertiesSet();
		userController = new UserController(registerUserUseCase, userRestMapper);
		mockMvc = MockMvcBuilders.standaloneSetup(userController)
			.setControllerAdvice(globalExceptionHandler)
			.setValidator(validatorFactoryBean)
			.build();
	}

	@Test
	void shouldRegisterUserAndReturnCreatedResponse() throws Exception {
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
		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Juan Perez\",\"email\":\"invalid-email\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("The request contains invalid data"))
			.andExpect(jsonPath("$.errors[0]").value("email: Email is invalid"));
	}

	@Test
	void shouldReturnConflictWhenEmailAlreadyExists() throws Exception {
		when(registerUserUseCase.register(any(RegisterUserCommand.class))).thenThrow(new DuplicateUserEmailException("Email is already in use"));

		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Juan Perez\",\"email\":\"juan@example.com\",\"password\":\"SecurePass123\"}"))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value("Email is already in use"));
	}
}