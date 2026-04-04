package com.sofka.user_service.domain.model;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record User(UUID id, UserName name, UserEmail email, String passwordHash, Instant createdAt) {

	public User {
		Objects.requireNonNull(id, "id must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(email, "email must not be null");
		Objects.requireNonNull(createdAt, "createdAt must not be null");
		if (passwordHash == null || passwordHash.isBlank()) {
			throw new InvalidUserDataException("Password hash is required");
		}
	}

	public static User register(UUID id, UserName name, UserEmail email, String passwordHash, Instant createdAt) {
		return new User(id, name, email, passwordHash, createdAt);
	}
}