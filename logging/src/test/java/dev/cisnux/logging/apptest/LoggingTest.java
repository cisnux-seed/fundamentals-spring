package dev.cisnux.logging.apptest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@SpringBootTest
@TestPropertySource(value = "classpath:/application-test.properties")
public class LoggingTest {

    @Test
    void testLongLogging() {
        for (int k = 0; k < 10; k++)
            for (int i = 0; i < 100_000; i++) {
                log.warn("Hello World {}", i);
            }
    }

    @Test
    void testLog() {
        log.info("Java");
        log.warn("Spring");
        log.error("Java Kotlin Go Dart");
    }
}
