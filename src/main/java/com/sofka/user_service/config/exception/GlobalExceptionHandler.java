package com.sofka.user_service.config.exception;

import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import com.sofka.user_service.domain.exception.InvalidUserDataException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidUserDataException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidUserDataException(InvalidUserDataException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(exception.getMessage(), List.of(exception.getMessage())));
	}

	@ExceptionHandler(DuplicateUserEmailException.class)
	public ResponseEntity<ApiErrorResponse> handleDuplicateUserEmailException(DuplicateUserEmailException exception) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorResponse(exception.getMessage(), List.of(exception.getMessage())));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult().getFieldErrors().stream()
			.map(this::formatFieldError)
			.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("The request contains invalid data", errors));
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}
}