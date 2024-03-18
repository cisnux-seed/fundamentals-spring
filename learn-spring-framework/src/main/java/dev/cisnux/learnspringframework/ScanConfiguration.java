package dev.cisnux.learnspringframework;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dev.cisnux.learnspringframework.configurations"})
public class ScanConfiguration {
}
