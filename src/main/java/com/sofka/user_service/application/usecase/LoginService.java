package com.sofka.user_service.application.usecase;

import com.sofka.user_service.application.port.input.LoginUseCase;
import com.sofka.user_service.application.port.output.JwtTokenIssuerPort;
import com.sofka.user_service.application.port.output.PasswordVerifierPort;
import com.sofka.user_service.application.port.output.UserRepositoryPort;
import com.sofka.user_service.domain.exception.InvalidCredentialsException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordVerifierPort passwordVerifierPort;
	private final JwtTokenIssuerPort jwtTokenIssuerPort;

	public LoginService(UserRepositoryPort userRepositoryPort, PasswordVerifierPort passwordVerifierPort, JwtTokenIssuerPort jwtTokenIssuerPort) {
		this.userRepositoryPort = Objects.requireNonNull(userRepositoryPort, "userRepositoryPort must not be null");
		this.passwordVerifierPort = Objects.requireNonNull(passwordVerifierPort, "passwordVerifierPort must not be null");
		this.jwtTokenIssuerPort = Objects.requireNonNull(jwtTokenIssuerPort, "jwtTokenIssuerPort must not be null");
	}

	@Override
	public LoginResult login(LoginCommand command) {
		Objects.requireNonNull(command, "command must not be null");

		User user = userRepositoryPort.findByEmail(new UserEmail(command.email()))
			.filter(foundUser -> passwordVerifierPort.matches(command.password(), foundUser.passwordHash()))
			.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

		return new LoginResult(
			jwtTokenIssuerPort.issueToken(user.id(), user.email().value()),
			"Bearer",
			jwtTokenIssuerPort.expirationSeconds(),
			new LoginResultUser(user.id(), user.name().value(), user.email().value())
		);
	}
}