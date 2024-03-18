package dev.cisnux.learnspringframework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextTest {
    @Test
    void testApplicationContext() {
        final var context = new AnnotationConfigApplicationContext(HelloWorldConfiguration.class);

        Assertions.assertNotNull(context);
    }
}
