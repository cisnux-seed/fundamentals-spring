package dev.cisnux.contactapi.controller;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.LoginRequest;
import dev.cisnux.contactapi.model.TokenResponse;
import dev.cisnux.contactapi.model.WebResponse;
import dev.cisnux.contactapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        final var tokenResponse = authService.login(loginRequest);
        return WebResponse.<TokenResponse>builder().data(tokenResponse).build();
    }

    @DeleteMapping(path = "/api/auth/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<String> logout(User user) {
        authService.logout(user);
        return WebResponse.<String>builder().data("OK").build();
    }
}
