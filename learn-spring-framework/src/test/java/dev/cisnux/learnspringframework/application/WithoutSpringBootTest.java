package dev.cisnux.learnspringframework.application;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class WithoutSpringBootTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(FooApplication.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testFoo() {
        final var foo = applicationContext.getBean(Foo.class);
        Assertions.assertNotNull(foo);
    }
}
