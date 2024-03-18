package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Car;
import dev.cisnux.learnspringframework.processor.IdGeneratorPostProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class BeanPostProcessorTest {
    @Configuration
    @Import(value = {Car.class, IdGeneratorPostProcessor.class})
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
    void testCar() {
        final var car = applicationContext.getBean(Car.class);

        System.out.println(car.getId());
        Assertions.assertNotNull(car.getId());
    }
}
