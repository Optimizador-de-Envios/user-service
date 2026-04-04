package com.sofka.user_service.infrastructure.adapter.output.persistence;

import com.sofka.user_service.infrastructure.adapter.output.persistence.entity.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

	Optional<UserEntity> findByEmail(String email);
}