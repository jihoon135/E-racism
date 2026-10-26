package io.github.jihoon135.api.etl;

import java.time.LocalDate;

/** CSV 한 행. status 는 CSV 에 적힌 값 그대로(검증 대상). */
public record RatificationRow(
        String countryName,
        String treatyCode,
        String status,
        LocalDate dateSignature,
        LocalDate dateRatification,
        LocalDate dateIndividualCommunications,
        LocalDate dateInquiryProcedure) {
}
