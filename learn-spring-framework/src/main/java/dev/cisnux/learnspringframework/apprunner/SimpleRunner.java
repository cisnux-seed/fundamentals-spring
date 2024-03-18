package dev.cisnux.learnspringframework.apprunner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SimpleRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        final var profiles = args.getOptionValues("profiles");
        log.info("Profiles : {}", profiles);
    }
}
