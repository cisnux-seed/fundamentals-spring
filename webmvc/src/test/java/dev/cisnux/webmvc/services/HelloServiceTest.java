package dev.cisnux.webmvc.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloServiceTest {

    @Autowired
    private HelloService service;

    @Test
    void hello() {
        assertEquals("Hello Guest", service.hello(null));
        assertEquals("Hello cisnux", service.hello("cisnux"));
    }
}