package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.configurations.BarConfiguration;
import dev.cisnux.learnspringframework.configurations.FooConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        FooConfiguration.class,
        BarConfiguration.class
})
public class MainConfiguration {
}
