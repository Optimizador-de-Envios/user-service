package com.sofka.user_service.infrastructure.adapter.output.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtTokenIssuerTest {

	@Test
	void shouldIssueJwtWithRequiredClaims() {
		JwtTokenIssuer tokenIssuer = new JwtTokenIssuer(Clock.fixed(Instant.parse("2026-04-03T18:30:00Z"), ZoneOffset.UTC), "test-secret", 86400L);
		String token = tokenIssuer.issueToken(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), "juan@example.com");

		String[] segments = token.split("\\.");
		assertEquals(3, segments.length);
		String payload = new String(Base64.getUrlDecoder().decode(segments[1]), StandardCharsets.UTF_8);
		assertTrue(payload.contains("\"iss\":\"user-service\""));
		assertTrue(payload.contains("\"sub\":\"c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82\""));
		assertTrue(payload.contains("\"email\":\"juan@example.com\""));
		assertTrue(payload.contains("\"iat\":"));
		assertTrue(payload.contains("\"exp\":"));
		assertEquals(86400L, tokenIssuer.expirationSeconds());
	}
}