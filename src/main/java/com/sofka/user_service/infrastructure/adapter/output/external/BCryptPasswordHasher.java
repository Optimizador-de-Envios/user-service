package com.sofka.user_service.infrastructure.adapter.output.external;

import com.sofka.user_service.application.port.output.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasherPort {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String hash(String password) {
		return passwordEncoder.encode(password);
	}
}