package com.sofka.user_service.domain.valueobject;

import com.sofka.user_service.domain.exception.InvalidUserDataException;
import java.util.Locale;
import java.util.regex.Pattern;

public record UserEmail(String value) {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

	public UserEmail {
		if (value == null || value.isBlank()) {
			throw new InvalidUserDataException("Email is invalid");
		}
		String normalizedValue = value.trim().toLowerCase(Locale.ROOT);
		if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
			throw new InvalidUserDataException("Email is invalid");
		}
		value = normalizedValue;
	}
}