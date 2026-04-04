package com.sofka.user_service.infrastructure.adapter.output.external;

import com.sofka.user_service.application.port.output.PasswordVerifierPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordVerifier implements PasswordVerifierPort {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public boolean matches(String rawPassword, String hashedPassword) {
		return passwordEncoder.matches(rawPassword, hashedPassword);
	}
}