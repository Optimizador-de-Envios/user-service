package com.sofka.user_service.application.port.input;

import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.domain.model.User;

public interface RegisterUserUseCase {

	User register(RegisterUserCommand command);
}