package dev.cisnux.learnspringframework;


import dev.cisnux.learnspringframework.data.Foo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeanNameConfiguration {
    @Primary
    @Bean(name = "firstFoo")
    public Foo foo1() {
        return new Foo();
    }

    @Bean(name = "secondFoo")
    public Foo foo2() {
        return new Foo();
    }
}
