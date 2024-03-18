package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BeanFactoryTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(ScanConfiguration.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    void testBeanFactory() {
        final var fooObjectProvider = applicationContext.getBeanProvider(Foo.class);
        final var fooList = fooObjectProvider.stream().toList();

        System.out.println(fooList);

        final var beans = applicationContext.getBeansOfType(Foo.class);
        System.out.println(beans);
    }
}
