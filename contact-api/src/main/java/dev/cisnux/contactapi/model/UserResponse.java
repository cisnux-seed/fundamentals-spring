package dev.cisnux.contactapi.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record UserResponse(String username, String name, Integer age) {
}
