package com.sofka.user_service.infrastructure.mapper;

import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import com.sofka.user_service.domain.valueobject.UserName;
import com.sofka.user_service.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

	public UserEntity toEntity(User user) {
		return new UserEntity(user.id(), user.name().value(), user.email().value(), user.passwordHash(), user.createdAt());
	}

	public User toDomain(UserEntity entity) {
		return User.register(entity.getId(), new UserName(entity.getName()), new UserEmail(entity.getEmail()), entity.getPasswordHash(), entity.getCreatedAt());
	}
}