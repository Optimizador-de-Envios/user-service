package com.sofka.user_service.application.usecase;

import com.sofka.user_service.application.port.input.RegisterUserUseCase;
import com.sofka.user_service.application.port.output.PasswordHasherPort;
import com.sofka.user_service.application.port.output.UserRepositoryPort;
import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.PlainPassword;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import java.time.Clock;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements RegisterUserUseCase {

	private final UserRepositoryPort userRepositoryPort;
	private final PasswordHasherPort passwordHasherPort;
	private final Clock clock;

	public RegisterUserService(UserRepositoryPort userRepositoryPort, PasswordHasherPort passwordHasherPort, Clock clock) {
		this.userRepositoryPort = Objects.requireNonNull(userRepositoryPort, "userRepositoryPort must not be null");
		this.passwordHasherPort = Objects.requireNonNull(passwordHasherPort, "passwordHasherPort must not be null");
		this.clock = Objects.requireNonNull(clock, "clock must not be null");
	}

	@Override
	public User register(RegisterUserCommand command) {
		Objects.requireNonNull(command, "command must not be null");

		UserName name = new UserName(command.name());
		UserEmail email = new UserEmail(command.email());
		PlainPassword password = new PlainPassword(command.password());

		if (userRepositoryPort.findByEmail(email).isPresent()) {
			throw new DuplicateUserEmailException("Email is already in use");
		}

		String passwordHash = passwordHasherPort.hash(password.value());
		User user = User.register(UUID.randomUUID(), name, email, passwordHash, clock.instant());
		return userRepositoryPort.save(user);
	}
}