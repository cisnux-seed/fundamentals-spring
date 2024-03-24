package dev.cisnux.contactapi.model;

import lombok.*;

@Builder
@With
public record WebResponse<T>(T data, String errors, PagingResponse pagingResponse) {
}
