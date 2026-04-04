package com.sofka.user_service.infrastructure.adapter.output.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import com.sofka.user_service.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.sofka.user_service.infrastructure.mapper.UserPersistenceMapper;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

	@Mock
	private UserJpaRepository userJpaRepository;

	private final UserPersistenceMapper mapper = new UserPersistenceMapper();

	@Test
	void shouldSaveDomainUserThroughJpaRepository() {
		UserPersistenceAdapter adapter = new UserPersistenceAdapter(userJpaRepository, mapper);
		User user = User.register(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), new UserName("Juan Perez"), new UserEmail("juan@example.com"), "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));
		when(userJpaRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

		User savedUser = adapter.save(user);

		verify(userJpaRepository).save(any(UserEntity.class));
		assertEquals(user, savedUser);
	}

	@Test
	void shouldFindDomainUserByEmailThroughJpaRepository() {
		UserPersistenceAdapter adapter = new UserPersistenceAdapter(userJpaRepository, mapper);
		UserEntity entity = new UserEntity(UUID.fromString("c6f5dd0d-55d7-4e52-a1cf-7cf7c26f4d82"), "Juan Perez", "juan@example.com", "hashed-password", Instant.parse("2026-04-03T18:30:00Z"));
		when(userJpaRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(entity));

		Optional<User> user = adapter.findByEmail(new UserEmail("juan@example.com"));

		assertTrue(user.isPresent());
		assertEquals(entity.getId(), user.get().id());
	}
}