package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Bar;
import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class ScopeTest {
    private ApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(ScopeConfiguration.class);
    }

    @Test
    void testPrototypeScope() {
        final var foo1 = applicationContext.getBean(Foo.class);
        final var foo2 = applicationContext.getBean(Foo.class);
        final var foo3 = applicationContext.getBean(Foo.class);

        Assertions.assertNotSame(foo1, foo2);
        Assertions.assertNotSame(foo1, foo3);
        Assertions.assertNotSame(foo2, foo3);
    }

    @Test
    void testDoublingScope() {
        final var bar1 = applicationContext.getBean(Bar.class);
        final var bar2 = applicationContext.getBean(Bar.class);
        final var bar3 = applicationContext.getBean(Bar.class);
        final var bar4 = applicationContext.getBean(Bar.class);

        Assertions.assertNotSame(bar1, bar2);
        Assertions.assertNotSame(bar3, bar4);

        Assertions.assertSame(bar1, bar3);
        Assertions.assertSame(bar2, bar4);
    }
}
