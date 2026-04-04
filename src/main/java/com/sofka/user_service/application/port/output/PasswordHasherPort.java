package com.sofka.user_service.application.port.output;

public interface PasswordHasherPort {

	String hash(String password);
}