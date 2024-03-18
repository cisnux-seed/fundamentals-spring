package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class PrimaryTest {
    private ApplicationContext applicationContext;

    @BeforeAll
    void beforeAll() {
        applicationContext = new AnnotationConfigApplicationContext(PrimaryConfiguration.class);
    }

    @Test
    void testGetPrimary() {
        final var foo = applicationContext.getBean(Foo.class);
        final var foo1 = applicationContext.getBean("foo1", Foo.class);
        final var foo2 = applicationContext.getBean("foo2", Foo.class);

        Assertions.assertSame(foo, foo1);
        Assertions.assertNotSame(foo, foo2);
        Assertions.assertNotSame(foo1, foo2);
    }
}
