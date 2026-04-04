package com.sofka.user_service.config.exception;

import java.util.List;

public record ApiErrorResponse(String message, List<String> errors) {
}