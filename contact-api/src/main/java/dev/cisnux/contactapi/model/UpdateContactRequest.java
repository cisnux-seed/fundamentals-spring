package dev.cisnux.contactapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UpdateContactRequest(
        @JsonIgnore
        @NotBlank
        String id,
        @NotBlank
        @Size(max = 100)
        String firstName,
        @Size(max = 100)
        String lastName,
        @Email
        @Size(max = 100)
        String email,
        @Size(max = 100)
        String phone
) {
}
