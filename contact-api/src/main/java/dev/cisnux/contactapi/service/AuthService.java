package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.LoginRequest;
import dev.cisnux.contactapi.model.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);
    void logout(User user);
}
