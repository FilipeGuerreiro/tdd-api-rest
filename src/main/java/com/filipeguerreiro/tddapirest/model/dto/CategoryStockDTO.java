package com.filipeguerreiro.tddapirest.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryStockDTO(
        @JsonProperty("category") String id,
        Long totalStock
) {
}
