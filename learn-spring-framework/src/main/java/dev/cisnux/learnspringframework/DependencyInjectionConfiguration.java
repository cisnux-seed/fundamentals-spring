package dev.cisnux.learnspringframework;

import dev.cisnux.learnspringframework.data.Bar;
import dev.cisnux.learnspringframework.data.Foo;
import dev.cisnux.learnspringframework.data.FooBar;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DependencyInjectionConfiguration {
    @Primary
    @Bean(name = "firstFoo")
    public Foo foo1() {
        return new Foo();
    }

    @Bean(name = "secondFoo")
    public Foo foo2() {
        return new Foo();
    }

    @Bean
    public Bar bar() {
        return new Bar();
    }

    @Bean
    public FooBar fooBar(@Qualifier(value = "secondFoo") Foo foo, Bar bar) {
        return new FooBar(foo, bar);
    }
}
