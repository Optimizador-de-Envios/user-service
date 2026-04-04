package com.sofka.user_service.infrastructure.adapter.output.external;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

class BCryptPasswordHasherTest {

	@Test
	void shouldHashPasswordUsingBcrypt() {
		BCryptPasswordHasher passwordHasher = new BCryptPasswordHasher();

		String hashedPassword = passwordHasher.hash("SecurePass123");

		assertNotEquals("SecurePass123", hashedPassword);
		assertTrue(BCrypt.checkpw("SecurePass123", hashedPassword));
	}
}