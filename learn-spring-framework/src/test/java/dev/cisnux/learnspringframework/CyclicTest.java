package dev.cisnux.learnspringframework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CyclicTest {
    @Test
    void cyclicTest() {
        Assertions.assertThrows(UnsatisfiedDependencyException.class, () -> new AnnotationConfigApplicationContext(CyclicConfiguration.class));
    }
}
