package dev.cisnux.config.runner;


import dev.cisnux.config.properties.ApplicationProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ApplicationPropertiesRunner implements ApplicationRunner {

    private ApplicationProperties properties;

    @Override
    public void run(ApplicationArguments args) {
        log.info(properties.getName());
        log.info(String.valueOf(properties.getVersion()));
        log.info(String.valueOf(properties.isProductionMode()));
    }
}