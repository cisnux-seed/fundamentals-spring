package dev.cisnux.contactapi.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record ContactResponse(String id, String firstName, String lastName, String email, String phone) {
}
