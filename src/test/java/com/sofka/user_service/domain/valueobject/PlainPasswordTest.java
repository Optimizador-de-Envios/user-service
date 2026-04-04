package com.sofka.user_service.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Test;

class PlainPasswordTest {

	@Test
	void shouldCreatePlainPasswordWhenLengthIsValid() {
		PlainPassword password = new PlainPassword("SecurePass123");

		assertEquals("SecurePass123", password.value());
	}

	@Test
	void shouldRejectShortPlainPassword() {
		InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () -> new PlainPassword("short"));

		assertEquals("Password must contain at least 8 characters", exception.getMessage());
	}
}