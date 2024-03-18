package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.data.FooBar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class OptionalTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(OptionalConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testOptional() {
        final var foo = applicationContext.getBean(Foo.class);
        final var fooBar = applicationContext.getBean(FooBar.class);

        Assertions.assertSame(foo, fooBar.getFoo());
        Assertions.assertNull(fooBar.getBar());
    }
}
