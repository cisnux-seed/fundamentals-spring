package dev.cisnux.aop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelloServiceTest {
    @Autowired
    private HelloService helloService;

    @Test
    void helloService() {
        Assertions.assertEquals("Hello Fajra", helloService.hello("Fajra"));
        Assertions.assertEquals("Hello Fajra Risqulla", helloService.hello("Fajra", "Risqulla"));
        Assertions.assertEquals("Bye Fajra", helloService.bye("Fajra"));
        helloService.test();
    }
}