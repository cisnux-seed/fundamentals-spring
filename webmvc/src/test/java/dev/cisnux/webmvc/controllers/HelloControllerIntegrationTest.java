package dev.cisnux.webmvc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControllerIntegrationTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void helloGuest() {
        final var responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/hello", String.class);
        final var statusCode = responseEntity.getStatusCode();
        final var response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals("Hello Guest", response.trim());
        assertEquals(200, statusCode.value());
    }

    @Test
    void helloName() {
        final var responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/hello?name=cisnux", String.class);
        final var statusCode = responseEntity.getStatusCode();
        final var response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals("Hello cisnux", response.trim());
        assertEquals(200, statusCode.value());
    }
}