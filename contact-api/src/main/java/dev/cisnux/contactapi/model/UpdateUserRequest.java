package dev.cisnux.contactapi.model;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UpdateUserRequest(
        @Size(max = 100)
        String name,
        @Size(max = 100)
        String password
) {
}
