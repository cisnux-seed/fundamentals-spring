package dev.cisnux.validation;

import dev.cisnux.validation.data.Foo;
import dev.cisnux.validation.data.Person;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PalindromeTest {
    @Autowired
    private Validator validator;

    @Test
    void palindromeValid() {
        final var constraintViolations = validator.validate(new Foo("makam"));
        Assertions.assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void palindromeInvalid() {
        final var constraintViolations = validator.validate(new Foo("fajra"));
        Assertions.assertFalse(constraintViolations.isEmpty());
        Assertions.assertEquals(1, constraintViolations.size());
    }

    @Test
    void palindromeInvalidMessage() {
        final var constraintViolations = validator.validate(new Foo("fajra"));
        Assertions.assertFalse(constraintViolations.isEmpty());
        Assertions.assertEquals(1, constraintViolations.size());

        constraintViolations.stream().findFirst().ifPresent(fooConstraintViolation ->
                Assertions.assertEquals("it's not valid", fooConstraintViolation.getMessage()));
    }
}
