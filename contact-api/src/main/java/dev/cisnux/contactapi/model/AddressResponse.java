package dev.cisnux.contactapi.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record AddressResponse(
        String id,
        String street,
        String city,
        String province,
        String country,
        String postalCode
) {
}
