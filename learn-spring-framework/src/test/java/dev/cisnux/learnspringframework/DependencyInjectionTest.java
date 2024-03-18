package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Bar;
import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.data.FooBar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class DependencyInjectionTest {
    private ApplicationContext applicationContext;

    @BeforeAll
    void beforeAll() {
        applicationContext = new AnnotationConfigApplicationContext(DependencyInjectionConfiguration.class);
    }

    @Test
    void testDI() {
        final var foo = applicationContext.getBean("secondFoo", Foo.class);
        final var bar = applicationContext.getBean(Bar.class);

        final var fooBar = applicationContext.getBean(FooBar.class);

        Assertions.assertSame(foo, fooBar.getFoo());
        Assertions.assertSame(bar, fooBar.getBar());
    }

    @Test
    void testNoDI() {
        final var foo = new Foo();
        final var bar = new Bar();

        final var fooBar = new FooBar(foo, bar);

        Assertions.assertSame(foo, fooBar.getFoo());
        Assertions.assertSame(bar, fooBar.getBar());
    }
}
