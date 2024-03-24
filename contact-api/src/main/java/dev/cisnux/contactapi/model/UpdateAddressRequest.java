package dev.cisnux.contactapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UpdateAddressRequest(
        @JsonIgnore
        @NotBlank
        String contactId,
        @NotBlank
        @JsonIgnore
        String addressId,
        @Size(max = 200)
        String street,
        @Size(max = 100)
        String city,
        @Size(max = 100)
        String province,
        @NotBlank
        @Size(max = 100)
        String country,
        @Size(max = 10)
        String postalCode
) {
}
