package com.sofka.user_service.application.port.output;

public interface PasswordVerifierPort {

	boolean matches(String rawPassword, String hashedPassword);
}