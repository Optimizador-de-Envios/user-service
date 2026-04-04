package com.sofka.user_service.domain.valueobject;

import com.sofka.user_service.domain.exception.InvalidUserDataException;

public record UserName(String value) {

	public UserName {
		if (value == null || value.isBlank()) {
			throw new InvalidUserDataException("Name is required");
		}
		value = value.trim();
	}
}