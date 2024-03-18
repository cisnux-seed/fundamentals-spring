package dev.cisnux.learnspringframework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FunctionalConfiguration {
    @Primary
    @Bean(name = "firstRunnable")
    Runnable runnable1() {
        return () -> System.out.println("Hi fajra!");
    }

    @Bean(name = "secondRunnable")
    Runnable runnable2() {
        return () -> System.out.println("Hi cisnux!");
    }
}
