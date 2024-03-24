package dev.cisnux.contactapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record RegisterUserRequest(
        @NotBlank
        @Size(max = 100)
        String username,
        @Size(max = 100)
        @NotBlank
        String password,
        @Size(max = 100)
        @NotBlank
        String name
) {

}
