package dev.cisnux.contactapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record LoginRequest(@NotBlank @Size(max = 100) String username, @Size(max = 100) String password) {
}
