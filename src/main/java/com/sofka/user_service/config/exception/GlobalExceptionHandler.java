package com.sofka.user_service.config.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.exception.InvalidCredentialsException;
import com.sofka.user_service.domain.exception.InvalidUserDataException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidUserDataException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidUserDataException(InvalidUserDataException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("INVALID_USER_DATA", exception.getMessage(), List.of(exception.getMessage())));
	}

	@ExceptionHandler(DuplicateUserEmailException.class)
	public ResponseEntity<ApiErrorResponse> handleDuplicateUserEmailException(DuplicateUserEmailException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse("DUPLICATE_EMAIL", exception.getMessage(), List.of(exception.getMessage())));
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException exception) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiErrorResponse("INVALID_CREDENTIALS", "The email or password is incorrect", List.of(exception.getMessage())));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult().getFieldErrors().stream()
			.map(this::formatFieldError)
			.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("VALIDATION_ERROR", "The request contains invalid data", errors));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		List<String> errors = new ArrayList<>();
		errors.add("Request body is missing, malformed, or has invalid field types");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("INVALID_JSON", "The request body is not readable", errors));
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}
}