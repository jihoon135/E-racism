package io.github.jihoon135.api.etl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** treaty → country → ratification 순으로 upsert. 한 트랜잭션이라 중간 실패 시 전부 롤백. */
@Component
public class SeedLoader {

    private static final String UPSERT_TREATY = """
            INSERT INTO treaty (code, name_en, name_ko, adopted_year, data_updated_at)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (code) DO UPDATE SET
                name_en = EXCLUDED.name_en,
                name_ko = EXCLUDED.name_ko,
                adopted_year = EXCLUDED.adopted_year,
                data_updated_at = EXCLUDED.data_updated_at
            """;

    // name_ko·region 은 이 단계에서 채우지 않으므로 갱신 대상에서 뺀다 (이후 수동 보강값 보존)
    private static final String UPSERT_COUNTRY = """
            INSERT INTO country (iso3, name_en)
            VALUES (?, ?)
            ON CONFLICT (iso3) DO UPDATE SET name_en = EXCLUDED.name_en
            """;

    private static final String UPSERT_RATIFICATION = """
            INSERT INTO ratification (iso3, treaty_code, status, date_signature, date_ratification,
                                      date_individual_communications, date_inquiry_procedure)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (iso3, treaty_code) DO UPDATE SET
                status = EXCLUDED.status,
                date_signature = EXCLUDED.date_signature,
                date_ratification = EXCLUDED.date_ratification,
                date_individual_communications = EXCLUDED.date_individual_communications,
                date_inquiry_procedure = EXCLUDED.date_inquiry_procedure
            """;

    private final JdbcTemplate jdbc;

    public SeedLoader(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** @param countries iso3 → name_en */
    @Transactional
    public void load(List<TreatySeed> treaties, Map<String, String> countries,
                     List<RatificationRow> rows, Map<String, String> nameToIso3) {
        jdbc.batchUpdate(UPSERT_TREATY, treaties, 100, (ps, t) -> {
            ps.setString(1, t.code());
            ps.setString(2, t.nameEn());
            ps.setString(3, t.nameKo());
            ps.setObject(4, t.adoptedYear());
            ps.setDate(5, sqlDate(t.dataUpdatedAt()));
        });
        jdbc.batchUpdate(UPSERT_COUNTRY, List.copyOf(countries.entrySet()), 500, (ps, e) -> {
            ps.setString(1, e.getKey());
            ps.setString(2, e.getValue());
        });
        jdbc.batchUpdate(UPSERT_RATIFICATION, rows, 500, (ps, r) -> {
            ps.setString(1, nameToIso3.get(r.countryName()));
            ps.setString(2, r.treatyCode());
            ps.setString(3, r.status());
            ps.setDate(4, sqlDate(r.dateSignature()));
            ps.setDate(5, sqlDate(r.dateRatification()));
            ps.setDate(6, sqlDate(r.dateIndividualCommunications()));
            ps.setDate(7, sqlDate(r.dateInquiryProcedure()));
        });
    }

    private static Date sqlDate(LocalDate d) {
        return d == null ? null : Date.valueOf(d);
    }
}
