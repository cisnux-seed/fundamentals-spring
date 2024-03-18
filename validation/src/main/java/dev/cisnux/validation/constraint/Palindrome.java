package dev.cisnux.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PalindromeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Palindrome {
  String message() default "{palindrome.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}