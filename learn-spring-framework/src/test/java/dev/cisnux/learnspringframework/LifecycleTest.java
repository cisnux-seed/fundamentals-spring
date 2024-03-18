package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Connection;
import dev.cisnux.learnspringframework.data.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class LifecycleTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(LifecycleConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @AfterAll
    void tearDown() {
//         applicationContext.close();
    }

    @Test
    void testConnection() {
        final var connection = applicationContext.getBean(Connection.class);
    }


    @Test
    void testServer() {
        final var server = applicationContext.getBean(Server.class);
    }
}
