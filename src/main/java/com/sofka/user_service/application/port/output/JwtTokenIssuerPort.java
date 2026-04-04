package com.sofka.user_service.application.port.output;

import java.util.UUID;

public interface JwtTokenIssuerPort {

	String issueToken(UUID userId, String email);

	long expirationSeconds();
}