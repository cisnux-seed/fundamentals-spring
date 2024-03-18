package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Bar;
import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ScanTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(ScanConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testImport() {
        final var foo = applicationContext.getBean(Foo.class);
        final var bar = applicationContext.getBean(Bar.class);

        Assertions.assertNotNull(foo);
        Assertions.assertNotNull(bar);
    }
}
