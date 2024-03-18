package dev.cisnux.learnspringframework.listeners;

import dev.cisnux.learnspringframework.events.SuccessfulLoginEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserListener {
    @EventListener(classes = SuccessfulLoginEvent.class)
    public void onLoginSuccessEvent(SuccessfulLoginEvent event){
        log.info("Success login again for user {}", event.getUser());
    }

    @EventListener(classes = SuccessfulLoginEvent.class)
    public void onLoginSuccessEvent2(SuccessfulLoginEvent event){
        log.info("Success login again for user {}", event.getUser());
    }

    @EventListener(classes = SuccessfulLoginEvent.class)
    public void onLoginSuccessEvent3(SuccessfulLoginEvent event){
        log.info("Success login again for user {}", event.getUser());
    }
}
