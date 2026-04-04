package com.sofka.user_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void shouldCreateUserWhenDataIsValid() {
		UUID userId = UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82");
		Instant createdAt = Instant.parse("2026-04-03T18:30:00Z");

		User user = User.register(userId, new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", createdAt);

		assertEquals(userId, user.id());
		assertEquals("Juan Perez", user.name().value());
		assertEquals("juan@example.com", user.email().value());
		assertEquals("hashed-password", user.passwordHash());
		assertEquals(createdAt, user.createdAt());
	}

	@Test
	void shouldRejectBlankPasswordHash() {
		UUID userId = UUID.randomUUID();
		Instant createdAt = Instant.now();

		InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () ->
			User.register(userId, new UserName("Juan Perez"), new UserEmail("juan@example.com"), " ", createdAt));

		assertEquals("Password hash is required", exception.getMessage());
	}
}