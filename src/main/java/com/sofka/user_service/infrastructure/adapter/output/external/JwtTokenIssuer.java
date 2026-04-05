package com.sofka.user_service.infrastructure.adapter.output.external;

import com.sofka.user_service.application.port.output.JwtTokenIssuerPort;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenIssuer implements JwtTokenIssuerPort {

	private final Clock clock;
	private final String secret;
	private final long expirationSeconds;

	public JwtTokenIssuer(Clock clock, @Value("${JWT_SECRET}") String secret, @Value("${JWT_EXPIRATION_SECONDS:86400}") long expirationSeconds) {
		this.clock = clock;
		this.secret = secret;
		this.expirationSeconds = expirationSeconds;
	}

	@Override
	public String issueToken(UUID userId, String email) {
		long issuedAt = clock.instant().getEpochSecond();
		long expiresAt = issuedAt + expirationSeconds;
		String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
		String payload = base64Url(String.format("{\"iss\":\"user-service\",\"sub\":\"%s\",\"email\":\"%s\",\"iat\":%d,\"exp\":%d}", userId, email, issuedAt, expiresAt));
		String signature = sign(header + "." + payload);
		return header + "." + payload + "." + signature;
	}

	@Override
	public long expirationSeconds() {
		return expirationSeconds;
	}

	private String base64Url(String value) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}

	private String sign(String value) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception exception) {
			throw new IllegalStateException("Unable to sign JWT", exception);
		}
	}
}