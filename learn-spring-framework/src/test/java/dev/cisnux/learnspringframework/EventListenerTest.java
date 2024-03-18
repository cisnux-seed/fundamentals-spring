package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.listeners.SuccessfulLoginAgainListener;
import dev.cisnux.learnspringframework.listeners.SuccessfulLoginListener;
import dev.cisnux.learnspringframework.listeners.UserListener;
import dev.cisnux.learnspringframework.services.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class EventListenerTest {
    @Configuration
    @Import(value = {UserService.class, SuccessfulLoginListener.class, SuccessfulLoginAgainListener.class, UserListener.class})
    // can't be private
    public static class TestConfiguration {

    }

    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testAware() {
        final var userService = applicationContext.getBean(UserService.class);
        userService.login("fajra", "fajra123");
        userService.login("fajra", "salah");
        userService.login("risqulla", "salah");
    }
}
