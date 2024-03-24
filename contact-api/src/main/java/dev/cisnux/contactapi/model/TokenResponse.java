package dev.cisnux.contactapi.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record TokenResponse(
        String token,
        Long expiredAt
) {
}
