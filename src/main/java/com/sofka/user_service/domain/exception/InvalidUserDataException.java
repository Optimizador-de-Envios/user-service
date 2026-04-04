package com.sofka.user_service.domain.exception;

public class InvalidUserDataException extends RuntimeException {

	public InvalidUserDataException(String message) {
		super(message);
	}
}