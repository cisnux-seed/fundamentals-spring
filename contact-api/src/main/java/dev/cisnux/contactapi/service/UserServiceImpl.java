package dev.cisnux.contactapi.service;

import dev.cisnux.contactapi.entity.User;
import dev.cisnux.contactapi.model.RegisterUserRequest;
import dev.cisnux.contactapi.model.UpdateUserRequest;
import dev.cisnux.contactapi.model.UserResponse;
import dev.cisnux.contactapi.repostory.UserRepository;
import dev.cisnux.contactapi.security.BCrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Transactional
    @Override
    public void register(RegisterUserRequest request) {
        validationService.validateObject(request);

        if (userRepository.existsById(request.username()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name has been used");

        final var user = new User();
        user.setUsername(request.username());
        user.setPassword(BCrypt.hashpw(request.password(), BCrypt.gensalt()));
        user.setName(request.name());
        user.setAge(request.age());
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserResponse getUser(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .age(user.getAge())
                .build();
    }

    @Transactional
    @Override
    public UserResponse updateUser(User user, UpdateUserRequest updateUserRequest) {
        validationService.validateObject(updateUserRequest);

        Optional.ofNullable(updateUserRequest.name())
                .ifPresent(user::setName);

        Optional.ofNullable(updateUserRequest.password())
                .ifPresent(password -> user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt())));

        userRepository.save(user);

        return UserResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .age(user.getAge())
                .build();
    }
}
