package dev.cisnux.learnspringframework.listeners;

import dev.cisnux.learnspringframework.events.SuccessfulLoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SuccessfulLoginListener implements ApplicationListener<SuccessfulLoginEvent> {
    @Override
    public void onApplicationEvent(SuccessfulLoginEvent event) {
        log.info("Success login for user {}", event.getUser());
    }
}
