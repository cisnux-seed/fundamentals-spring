package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanTest {
    @Test
    void testCreateBean() {
        final var context = new AnnotationConfigApplicationContext(BeanConfiguration.class);

        Assertions.assertNotNull(context);
    }

    @Test
    void testGetBean() {
        final var context = new AnnotationConfigApplicationContext(BeanConfiguration.class);
        final var foo1 = context.getBean(Foo.class);
        final var foo2 = context.getBean(Foo.class);

        Assertions.assertSame(foo1, foo2);
    }
}
