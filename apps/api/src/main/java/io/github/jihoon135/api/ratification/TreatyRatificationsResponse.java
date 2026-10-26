package io.github.jihoon135.api.ratification;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record TreatyRatificationsResponse(
        @Schema(example = "ICERD") String treatyCode,
        @Schema(description = "조약 원본 기준일", example = "2026-06-02", nullable = true) LocalDate dataUpdatedAt,
        List<RatificationItem> ratifications) {
}
