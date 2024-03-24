package dev.cisnux.contactapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record SearchContactRequest(
        String name,
        String email,
        String phone,
        @NotNull
        Integer page,
        @NotNull
        Integer size
) {
}