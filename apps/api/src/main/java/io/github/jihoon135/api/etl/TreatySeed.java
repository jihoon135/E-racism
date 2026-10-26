package io.github.jihoon135.api.etl;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

/** treaties.json 의 조약 한 건. */
public record TreatySeed(
        String code,
        @JsonProperty("name_en") String nameEn,
        @JsonProperty("name_ko") String nameKo,
        @JsonProperty("adopted_year") Integer adoptedYear,
        @JsonProperty("data_updated_at") LocalDate dataUpdatedAt) {
}
