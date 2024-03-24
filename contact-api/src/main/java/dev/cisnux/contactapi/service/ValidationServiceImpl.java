package dev.cisnux.contactapi.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final Validator validator;

    @Override
    public <T> void validateObject(T object) {
        final var constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) throw new ConstraintViolationException(constraintViolations);
    }
}
