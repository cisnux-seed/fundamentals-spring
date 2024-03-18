package dev.cisnux.validation.helper;

import org.springframework.stereotype.Component;

@Component
public class GreetingImpl implements Greeting {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
