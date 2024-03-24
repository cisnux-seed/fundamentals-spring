package dev.cisnux.contactapi.controller;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.RegisterUserRequest;
import dev.cisnux.contactapi.model.UpdateUserRequest;
import dev.cisnux.contactapi.model.UserResponse;
import dev.cisnux.contactapi.model.WebResponse;
import dev.cisnux.contactapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping(
            path = "/api/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        service.register(request);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<UserResponse> get(User user) {
        final var userResponse = service.getUser(user);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }

    @PatchMapping(
            path = "/api/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public WebResponse<UserResponse> update(User user, @RequestBody UpdateUserRequest updateUserRequest){
        final var userResponse = service.updateUser(user, updateUserRequest);
        return WebResponse.<UserResponse>builder().data(userResponse).build();
    }
}
