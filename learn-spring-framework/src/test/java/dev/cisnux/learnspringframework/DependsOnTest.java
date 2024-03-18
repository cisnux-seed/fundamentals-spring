package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class DependsOnTest {
    private ApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        // ignore lazy foo
        applicationContext = new AnnotationConfigApplicationContext(DependsOnConfiguration.class);
    }

    @Test
    void testDependsOn() {
        // create a new bar first
        // then create a lazy foo
        final var foo = applicationContext.getBean(Foo.class);
    }
}
