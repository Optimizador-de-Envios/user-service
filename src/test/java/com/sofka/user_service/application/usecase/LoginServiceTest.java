package com.sofka.user_service.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sofka.user_service.application.port.output.JwtTokenIssuerPort;
import com.sofka.user_service.application.port.output.PasswordVerifierPort;
import com.sofka.user_service.application.port.output.UserRepositoryPort;
import com.sofka.user_service.domain.exception.InvalidCredentialsException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

	@Mock
	private UserRepositoryPort userRepositoryPort;

	@Mock
	private PasswordVerifierPort passwordVerifierPort;

	@Mock
	private JwtTokenIssuerPort jwtTokenIssuerPort;

	@Test
	void shouldLoginWhenCredentialsAreValid() {
		LoginService loginService = new LoginService(userRepositoryPort, passwordVerifierPort, jwtTokenIssuerPort);
		User user = User.register(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));
		when(userRepositoryPort.findByEmail(new UserEmail("juan@example.com"))).thenReturn(Optional.of(user));
		when(passwordVerifierPort.matches("SecurePass123", "hashed-password")).thenReturn(true);
		when(jwtTokenIssuerPort.issueToken(user.id(), user.email().value())).thenReturn("jwt-token");
		when(jwtTokenIssuerPort.expirationSeconds()).thenReturn(86400L);

		LoginResult result = loginService.login(new LoginCommand("juan@example.com", "SecurePass123"));

		assertEquals("jwt-token", result.accessToken());
		assertEquals("Bearer", result.tokenType());
		assertEquals(86400L, result.expiresIn());
		assertEquals(user.id(), result.user().id());
		assertEquals("Juan Perez", result.user().name());
		assertEquals("juan@example.com", result.user().email());
		verify(jwtTokenIssuerPort).issueToken(user.id(), user.email().value());
	}

	@Test
	void shouldRejectInvalidCredentials() {
		LoginService loginService = new LoginService(userRepositoryPort, passwordVerifierPort, jwtTokenIssuerPort);
		User user = User.register(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));
		when(userRepositoryPort.findByEmail(new UserEmail("juan@example.com"))).thenReturn(Optional.of(user));
		when(passwordVerifierPort.matches("wrong-password", "hashed-password")).thenReturn(false);

		InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class, () -> loginService.login(new LoginCommand("juan@example.com", "wrong-password")));

		assertEquals("Invalid credentials", exception.getMessage());
		verify(jwtTokenIssuerPort, org.mockito.Mockito.never()).issueToken(any(), any());
	}
}