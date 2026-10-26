package io.github.jihoon135.api.etl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * 비준 CSV 파서. 이 파일은 따옴표 필드가 없고 컬럼이 7개로 고정이라 단순 split 으로 충분하다.
 * 따옴표가 나타나거나 컬럼 수가 다르면 조용히 넘어가지 않고 예외로 중단한다.
 */
@Component
public class RatificationCsvReader {

    private static final List<String> HEADER = List.of(
            "country_name", "treaty_code", "status", "date_signature", "date_ratification",
            "date_individual_communications", "date_inquiry_procedure");

    public List<RatificationRow> read(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            throw new IllegalStateException("빈 CSV: " + file);
        }
        String header = lines.get(0).replace("﻿", "");
        if (!Arrays.asList(header.split(",", -1)).equals(HEADER)) {
            throw new IllegalStateException("CSV 헤더 불일치: " + header);
        }
        List<RatificationRow> rows = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            if (line.contains("\"")) {
                throw new IllegalStateException("따옴표 필드는 지원하지 않음 (line " + (i + 1) + "): " + line);
            }
            String[] f = line.split(",", -1);
            if (f.length != HEADER.size()) {
                throw new IllegalStateException("컬럼 수 불일치 (line " + (i + 1) + "): " + line);
            }
            rows.add(new RatificationRow(f[0], f[1], f[2], date(f[3]), date(f[4]), date(f[5]), date(f[6])));
        }
        return rows;
    }

    private static LocalDate date(String s) {
        return s.isBlank() ? null : LocalDate.parse(s);
    }
}
