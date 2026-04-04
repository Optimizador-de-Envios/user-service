package com.sofka.user_service.domain.valueobject;

import com.sofka.user_service.domain.exception.InvalidUserDataException;

public record PlainPassword(String value) {

	public PlainPassword {
		if (value == null || value.isBlank() || value.length() < 8) {
			throw new InvalidUserDataException("Password must contain at least 8 characters");
		}
	}
}