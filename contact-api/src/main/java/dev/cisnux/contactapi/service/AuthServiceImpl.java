package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.LoginRequest;
import dev.cisnux.contactapi.model.TokenResponse;
import dev.cisnux.contactapi.repostory.UserRepository;
import dev.cisnux.contactapi.security.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.Period;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        validationService.validateObject(loginRequest);
        final var user = userRepository.findById(loginRequest.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "name or password is wrong"));

        if (!BCrypt.checkpw(loginRequest.password(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user or password is wrong");

        user.setToken(UUID.randomUUID().toString());
        user.setTokenExpired(
                Instant
                        .now()
                        .plus(Period.ofDays(30))
                        .toEpochMilli()
        );
        userRepository.save(user);

        return TokenResponse
                .builder()
                .token(user.getToken())
                .expiredAt(user.getTokenExpired())
                .build();
    }

    @Transactional
    @Override
    public void logout(User user) {
        user.setToken(null);
        user.setTokenExpired(null);

        userRepository.save(user);
    }
}
