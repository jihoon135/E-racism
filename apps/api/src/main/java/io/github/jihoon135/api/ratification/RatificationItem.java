package io.github.jihoon135.api.ratification;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

/** 지구본 색칠용 경량 항목. 다른 날짜는 국가 상세 API 범위. */
public record RatificationItem(
        @Schema(example = "KOR") String iso3,
        @Schema(allowableValues = {"STATE_PARTY", "SIGNATORY", "NO_ACTION"}) String status,
        @Schema(example = "1978-12-05", nullable = true) LocalDate dateRatification) {
}
