package com.sofka.user_service.application.port.output;

import com.sofka.user_service.domain.model.User;
import com.sofka.user_service.domain.valueobject.UserEmail;
import java.util.Optional;

public interface UserRepositoryPort {

	Optional<User> findByEmail(UserEmail email);

	User save(User user);
}