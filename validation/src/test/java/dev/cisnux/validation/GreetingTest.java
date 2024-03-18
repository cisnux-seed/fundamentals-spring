package dev.cisnux.validation;

import dev.cisnux.validation.data.Person;
import dev.cisnux.validation.helper.Greeting;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GreetingTest {
    @Autowired
    private Validator validator;
    @Autowired
    private Greeting greeting;

    @Test
    void testSuccess() {
        String message = greeting.sayHello("Fajra");
        Assertions.assertEquals("Hello Fajra", message);
    }

    @Test
    void testError() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            greeting.sayHello("");
        });
    }
}
