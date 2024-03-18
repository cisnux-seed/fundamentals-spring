package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.FunctionalBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class FunctionalTest {
    private ConfigurableApplicationContext applicationContext;

    @BeforeAll
    void beforeAll() {
        this.applicationContext = new AnnotationConfigApplicationContext(FunctionalComponent.class);
    }

    @Test
    void test() {
        final var functionalBean = applicationContext.getBean(FunctionalBean.class);
        Assertions.assertNotNull(functionalBean);
    }
}
