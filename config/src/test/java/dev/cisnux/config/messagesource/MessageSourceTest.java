package dev.cisnux.config.messagesource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageSourceTest {
    private ConfigurableApplicationContext applicationContext;

    private MessageSource messageSource;

    @BeforeAll
    void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
        messageSource = applicationContext.getBean(MessageSource.class);
    }

    @Test
    void testDefaultLocale() {
        final var message = messageSource.getMessage("hello", new Object[]{"Cisnux"}, Locale.ENGLISH);
        System.out.println(message);
        Assertions.assertEquals("Hello Cisnux", message);
    }

    @Test
    void testIndonesianLocale() {
        final var message = messageSource.getMessage("hello", new Object[]{"Cisnux"}, Locale.of("id", "ID"));
        System.out.println(message);
        Assertions.assertEquals("Halo Cisnux", message);
    }

    @SpringBootApplication
    public static class TestApplication {

        @Bean
        public MessageSource messageSource() {
            final var messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename("my");
            return messageSource;
        }
    }
}
