package com.sofka.user_service.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Test;

class UserEmailTest {

	@Test
	void shouldCreateUserEmailWhenValueIsValid() {
		UserEmail userEmail = new UserEmail("juan@example.com");

		assertEquals("juan@example.com", userEmail.value());
	}

	@Test
	void shouldRejectInvalidEmailFormat() {
		InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () -> new UserEmail("invalid-email"));

		assertEquals("Email is invalid", exception.getMessage());
	}
}