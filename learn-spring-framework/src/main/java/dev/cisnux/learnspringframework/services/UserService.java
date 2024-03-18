package dev.cisnux.learnspringframework.services;

import dev.cisnux.learnspringframework.data.User;
import dev.cisnux.learnspringframework.events.SuccessfulLoginEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.Objects;

public class UserService implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean login(String username, String password) {
        final var isLoginSuccess = isLoginSuccess(username, password);

        if (isLoginSuccess)
            applicationEventPublisher.publishEvent(new SuccessfulLoginEvent(new User(username)));

        return isLoginSuccess;
    }

    private boolean isLoginSuccess(String username, String password) {
        return Objects.equals("fajra", username) && Objects.equals("fajra123", password);
    }
}
