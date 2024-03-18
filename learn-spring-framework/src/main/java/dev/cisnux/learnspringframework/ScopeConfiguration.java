package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Bar;
import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.scope.DoublingScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class ScopeConfiguration {
    @Bean
    // prototype will be lazy
    @Scope("prototype")
    public Foo foo() {
        log.info("Create new Foo");
        return new Foo();
    }

    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        configurer.addScope("doubling", new DoublingScope());
        return configurer;
    }

    @Bean
    @Scope(value = "doubling")
    public Bar bar() {
        log.info("Create new Bar");
        return new Bar();
    }
}
