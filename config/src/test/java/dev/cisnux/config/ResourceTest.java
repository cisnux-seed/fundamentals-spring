package dev.cisnux.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class ResourceTest {
    @Test
    void testResource() throws IOException {
        final var resource = new ClassPathResource("/text/sample.txt");
        System.out.println(new String(resource.getInputStream().readAllBytes()));

        Assertions.assertNotNull(resource);
        Assertions.assertTrue(resource.exists());
        Assertions.assertNotNull(resource.getFile());
    }
}
