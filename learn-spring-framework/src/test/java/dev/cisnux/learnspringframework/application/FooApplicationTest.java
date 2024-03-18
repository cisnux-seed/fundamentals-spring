package dev.cisnux.learnspringframework.application;

import dev.cisnux.learnspringframework.data.Foo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {FooApplication.class})
class FooApplicationTest {
    @Autowired
    private Foo foo;

    @Test
    void testSpringBoot() {
        assertNotNull(foo);
    }
}