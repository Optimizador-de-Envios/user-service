package com.sofka.user_service.infrastructure.adapter.output.persistence;

import com.sofka.user_service.application.port.output.UserRepositoryPort;
import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.infrastructure.mapper.UserPersistenceMapper;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserRepositoryPort {

	private final UserJpaRepository userJpaRepository;
	private final UserPersistenceMapper userPersistenceMapper;

	public UserPersistenceAdapter(UserJpaRepository userJpaRepository, UserPersistenceMapper userPersistenceMapper) {
		this.userJpaRepository = Objects.requireNonNull(userJpaRepository, "userJpaRepository must not be null");
		this.userPersistenceMapper = Objects.requireNonNull(userPersistenceMapper, "userPersistenceMapper must not be null");
	}

	@Override
	public Optional<User> findByEmail(UserEmail email) {
		return userJpaRepository.findByEmail(email.value()).map(userPersistenceMapper::toDomain);
	}

	@Override
	public User save(User user) {
		return userPersistenceMapper.toDomain(userJpaRepository.save(userPersistenceMapper.toEntity(user)));
	}
}