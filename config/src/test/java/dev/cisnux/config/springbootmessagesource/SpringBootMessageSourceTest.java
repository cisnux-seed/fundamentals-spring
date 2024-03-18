package dev.cisnux.config.springbootmessagesource;

import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

@SpringBootTest(classes = {SpringBootMessageSourceTest.TestApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpringBootMessageSourceTest {
    @Autowired
    private TestApplication.SampleSource sampleSource;

    @Test
    void test() {
        Assertions.assertEquals("Hello Cisnux", sampleSource.greeting(Locale.ENGLISH));
        Assertions.assertEquals("Halo Cisnux", sampleSource.greeting(Locale.of("id", "ID")));
    }

    @SpringBootApplication
    public static class TestApplication {

        @Component
        @Setter
        public static class SampleSource implements MessageSourceAware {
            private MessageSource messageSource;

            public String greeting(Locale locale) {
                return messageSource.getMessage("hello", new Object[]{"Cisnux"}, locale);
            }
        }
    }
}
