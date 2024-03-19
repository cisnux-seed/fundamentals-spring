package dev.cisnux.webmvc.services;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + Optional.ofNullable(name).orElse("Guest");
    }
}
