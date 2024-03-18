package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.MultiFoo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "dev.cisnux.learnspringframework.services",
        "dev.cisnux.learnspringframework.repositories",
        "dev.cisnux.learnspringframework.configurations",
        // can be like this
//        "dev.cisnux.learnspringframework.data"
})
// or like this
@Import(value = {MultiFoo.class})
public class ComponentConfiguration {
}
