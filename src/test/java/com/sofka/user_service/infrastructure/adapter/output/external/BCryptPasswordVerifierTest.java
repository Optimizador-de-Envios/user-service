package com.sofka.user_service.infrastructure.adapter.output.external;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class BCryptPasswordVerifierTest {

	@Test
	void shouldMatchPasswordAgainstHash() {
		BCryptPasswordVerifier passwordVerifier = new BCryptPasswordVerifier();
		String hashedPassword = new BCryptPasswordEncoder().encode("SecurePass123");

		assertTrue(passwordVerifier.matches("SecurePass123", hashedPassword));
		assertFalse(passwordVerifier.matches("wrong", hashedPassword));
	}
}