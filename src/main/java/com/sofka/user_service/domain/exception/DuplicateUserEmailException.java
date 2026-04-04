package com.sofka.user_service.domain.exception;

public class DuplicateUserEmailException extends RuntimeException {

	public DuplicateUserEmailException(String message) {
		super(message);
	}
}