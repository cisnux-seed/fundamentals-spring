package dev.cisnux.contactapi.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record PagingResponse(Integer currentPage, Integer totalPage, Integer size) {
}
