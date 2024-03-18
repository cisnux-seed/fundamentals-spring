package dev.cisnux.learnspringframework.events;

import dev.cisnux.learnspringframework.data.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SuccessfulLoginEvent extends ApplicationEvent {
    private final User user;

    public SuccessfulLoginEvent(User user) {
        super(user);
        this.user = user;
    }
}
