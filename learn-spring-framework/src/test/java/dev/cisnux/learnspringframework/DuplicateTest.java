package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DuplicateTest {
    @Test
    void testDuplicate() {
        final var context = new AnnotationConfigApplicationContext(DuplicateConfiguration.class);

        Assertions.assertThrows(NoUniqueBeanDefinitionException.class, () -> context.getBean(Foo.class));
    }

    @Test
    void getBean() {
        final var context = new AnnotationConfigApplicationContext(DuplicateConfiguration.class);

        final var foo1 = context.getBean("foo1");
        final var foo2 = context.getBean("foo2");
        final var foo3 = context.getBean("foo1");
        final var foo4 = context.getBean("foo2");

        Assertions.assertNotSame(foo1, foo2);
        Assertions.assertNotSame(foo3, foo4);

        Assertions.assertSame(foo1, foo3);
        Assertions.assertSame(foo2, foo4);
    }
}
