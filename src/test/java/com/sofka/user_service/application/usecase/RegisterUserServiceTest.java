package com.sofka.user_service.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sofka.user_service.application.port.output.PasswordHasherPort;
import com.sofka.user_service.application.port.output.UserRepositoryPort;
import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Mock
	private PasswordHasherPort passwordHasherPort;

	@Captor
	private ArgumentCaptor<User> userCaptor;

	@Test
	void shouldRegisterUserWhenEmailIsAvailable() {
		Clock clock = Clock.fixed(Instant.parse("2026-04-03T18:30:00Z"), ZoneOffset.UTC);
		RegisterUserService registerUserService = new RegisterUserService(userRepositoryPort, passwordHasherPort, clock);
		RegisterUserCommand command = new RegisterUserCommand("Juan Perez", "juan@example.com", "SecurePass123");

		when(userRepositoryPort.findByEmail(new UserEmail("juan@example.com"))).thenReturn(Optional.empty());
		when(passwordHasherPort.hash("SecurePass123")).thenReturn("hashed-password");
		when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User savedUser = registerUserService.register(command);

		verify(passwordHasherPort).hash("SecurePass123");
		verify(userRepositoryPort).save(userCaptor.capture());
		assertEquals(savedUser, userCaptor.getValue());
		assertEquals("Juan Perez", savedUser.name().value());
		assertEquals("juan@example.com", savedUser.email().value());
		assertEquals("hashed-password", savedUser.passwordHash());
		assertEquals(Instant.parse("2026-04-03T18:30:00Z"), savedUser.createdAt());
	}

	@Test
	void shouldRejectRegistrationWhenEmailAlreadyExists() {
		Clock clock = Clock.fixed(Instant.parse("2026-04-03T18:30:00Z"), ZoneOffset.UTC);
		RegisterUserService registerUserService = new RegisterUserService(userRepositoryPort, passwordHasherPort, clock);
		RegisterUserCommand command = new RegisterUserCommand("Juan Perez", "juan@example.com", "SecurePass123");

		when(userRepositoryPort.findByEmail(new UserEmail("juan@example.com"))).thenReturn(Optional.of(
			User.register(UUID.randomUUID(), new UserName("Existing User"), new UserEmail("juan@example.com"), "hashed", Instant.parse("2026-04-03T18:00:00Z"))));

		DuplicateUserEmailException exception = assertThrows(DuplicateUserEmailException.class, () -> registerUserService.register(command));

		assertEquals("Email is already in use", exception.getMessage());
		verify(passwordHasherPort, never()).hash(any());
		verify(userRepositoryPort, never()).save(any());
	}
}