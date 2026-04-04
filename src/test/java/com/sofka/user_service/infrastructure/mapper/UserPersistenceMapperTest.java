package com.sofka.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import com.sofka.user_service.infrastructure.adapter.output.persistence.entity.UserEntity;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserPersistenceMapperTest {

	private final UserPersistenceMapper mapper = new UserPersistenceMapper();

	@Test
	void shouldMapDomainUserToEntity() {
		User user = User.register(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));

		UserEntity entity = mapper.toEntity(user);

		assertEquals(user.id(), entity.getId());
		assertEquals("Juan Perez", entity.getName());
		assertEquals("juan@example.com", entity.getEmail());
		assertEquals("hashed-password", entity.getPasswordHash());
		assertEquals(user.createdAt(), entity.getCreatedAt());
	}

	@Test
	void shouldMapEntityToDomainUser() {
		UserEntity entity = new UserEntity(
			UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"),
			"Juan Perez",
			"juan@example.com",
			"hashed-password",
			Instant.parse("2026-04-03T18:30:00Z"));

		User user = mapper.toDomain(entity);

		assertEquals(entity.getId(), user.id());
		assertEquals(entity.getName(), user.name().value());
		assertEquals(entity.getEmail(), user.email().value());
		assertEquals(entity.getPasswordHash(), user.passwordHash());
		assertEquals(entity.getCreatedAt(), user.createdAt());
	}
}