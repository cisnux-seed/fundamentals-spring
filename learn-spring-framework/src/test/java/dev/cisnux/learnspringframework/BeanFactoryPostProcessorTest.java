package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.processor.FooBeanFactoryPostProcessor;
import dev.cisnux.learnspringframework.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BeanFactoryPostProcessorTest {
    @Configuration
    @Import(value = {FooBeanFactoryPostProcessor.class})
    // can't be private
    public static class TestConfiguration {

    }

    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(TestConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testAware() {
        final var foo = applicationContext.getBean(Foo.class);
        final var foo1 = applicationContext.getBean(Foo.class);
        Assertions.assertNotNull(foo);
        Assertions.assertSame(foo, foo1);
    }
}
