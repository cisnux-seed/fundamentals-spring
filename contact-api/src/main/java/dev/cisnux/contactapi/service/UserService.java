package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.RegisterUserRequest;
import dev.cisnux.contactapi.model.UpdateUserRequest;
import dev.cisnux.contactapi.model.UserResponse;

public interface UserService {
    void register(RegisterUserRequest request);

    UserResponse getUser(User user);

    UserResponse updateUser(User user, UpdateUserRequest updateUserRequest);
}
