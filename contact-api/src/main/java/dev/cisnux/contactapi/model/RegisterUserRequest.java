package dev.cisnux.contactapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;
import org.hibernate.validator.constraints.Range;

@Builder
@With
public record RegisterUserRequest(@NotBlank @Size(max = 100) String username,
                                  @Size(max = 100) @NotBlank String password, @Size(max = 100) @NotBlank String name,
                                  @Range(min = 17, max = 127) @NotNull Integer age) {

}
