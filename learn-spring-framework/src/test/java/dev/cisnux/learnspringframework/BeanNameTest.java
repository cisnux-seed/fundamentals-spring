package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BeanNameTest {
    private AnnotationConfigApplicationContext applicationContext;


    @BeforeAll
    void beforeAll() {
        applicationContext = new AnnotationConfigApplicationContext(BeanNameConfiguration.class);
    }

    @Test
    void testBeanName() {
        final var foo = applicationContext.getBean(Foo.class);
        final var fooFirst = applicationContext.getBean("firstFoo", Foo.class);
        final var fooSecond = applicationContext.getBean("secondFoo", Foo.class);

        Assertions.assertSame(foo, fooFirst);
        Assertions.assertNotSame(foo, fooSecond);
    }
}
