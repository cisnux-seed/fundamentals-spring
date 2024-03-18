package dev.cisnux.learnspringframework.commandapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class LogCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        log.info("Log Command Line Runner : {}", Arrays.toString(args));
    }
}
