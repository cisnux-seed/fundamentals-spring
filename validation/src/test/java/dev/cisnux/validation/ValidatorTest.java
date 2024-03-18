package dev.cisnux.validation;

import dev.cisnux.validation.data.Person;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ValidatorTest {
    @Autowired
    private Validator validator;

    @Test
    void personNotValid() {
        final var person = new Person("", "");

        final var constraintViolations = validator.validate(person);
        Assertions.assertFalse(constraintViolations.isEmpty());
        Assertions.assertEquals(2, constraintViolations.size());
    }

    @Test
    void personValid() {
        final var person = new Person("1", "Fajra");

        final var constraintViolations = validator.validate(person);
        Assertions.assertTrue(constraintViolations.isEmpty());
    }
}
