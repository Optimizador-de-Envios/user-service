package com.sofka.user_service.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Test;

class UserNameTest {

	@Test
	void shouldCreateUserNameWhenValueIsValid() {
		UserName userName = new UserName("Juan Perez");

		assertEquals("Juan Perez", userName.value());
	}

	@Test
	void shouldRejectBlankUserName() {
		InvalidUserDataException exception = assertThrows(InvalidUserDataException.class, () -> new UserName("   "));

		assertEquals("Name is required", exception.getMessage());
	}
}