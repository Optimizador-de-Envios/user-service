package com.sofka.user_service.infrastructure.adapter.output.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sofka.user_service.infrastructure.adapter.output.persistence.entity.UserEntity;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserJpaRepositoryTest {

	@Test
	void shouldCreateUserEntityWithConstructorValues() {
		UserEntity entity = new UserEntity(UUID.randomUUID(), "Juan Perez", "juan@example.com", "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));

		assertEquals(entity.getId(), entity.getId());
		assertEquals("Juan Perez", entity.getName());
		assertEquals("juan@example.com", entity.getEmail());
		assertEquals("hashed-password", entity.getPasswordHash());
		assertEquals(Instant.parse("2026-04-03T18:30:00Z"), entity.getCreatedAt());
	}
}