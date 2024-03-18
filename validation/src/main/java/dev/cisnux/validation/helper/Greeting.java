package dev.cisnux.validation.helper;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public interface Greeting {

  String sayHello(@NotBlank String name);
}