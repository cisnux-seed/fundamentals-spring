package dev.cisnux.validation.data;

import jakarta.validation.constraints.NotBlank;

public record Person(@NotBlank String id, @NotBlank String name) {
}
