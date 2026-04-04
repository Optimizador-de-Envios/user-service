package com.sofka.user_service.application.port.input;

import com.sofka.user_service.application.usecase.LoginCommand;
import com.sofka.user_service.application.usecase.LoginResult;

public interface LoginUseCase {

	LoginResult login(LoginCommand command);
}