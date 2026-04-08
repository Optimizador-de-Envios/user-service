package com.sofka.user_service.config;

import com.sofka.user_service.application.port.input.RegisterUserUseCase;
import com.sofka.user_service.application.usecase.RegisterUserCommand;
import com.sofka.user_service.domain.exception.DuplicateUserEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds QA test users on startup when {@code SEED_TEST_DATA=true}.
 * Safe to run multiple times: duplicate emails are silently ignored.
 * Never enable this in production environments.
 */
@Component
public class TestDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(TestDataSeeder.class);

    @Value("${SEED_TEST_DATA:false}")
    private String seedTestData;

    private final RegisterUserUseCase registerUserUseCase;

    public TestDataSeeder(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!"true".equalsIgnoreCase(seedTestData)) {
            log.debug("[TestDataSeeder] seed disabled (SEED_TEST_DATA={})", seedTestData);
            return;
        }
        seedUser("QA Usuario",   "qa.usuario@example.com",  "Password123");
        seedUser("QA Usuario B", "qa.usuariob@example.com", "Password123");
    }

    private void seedUser(String name, String email, String password) {
        try {
            registerUserUseCase.register(new RegisterUserCommand(name, email, password));
            log.info("[TestDataSeeder] created test user: {}", email);
        } catch (DuplicateUserEmailException e) {
            log.info("[TestDataSeeder] test user already exists, skipping: {}", email);
        }
    }
}
