package io.github.jihoon135.api.etl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** CSV 의 status(유도값)를 날짜로 재계산해 비교한다. 규칙은 docs/01 §2-1. */
@Component
public class StatusVerifier {

    private static final Logger log = LoggerFactory.getLogger(StatusVerifier.class);

    public static String derive(RatificationRow r) {
        if (r.dateRatification() != null) {
            return "STATE_PARTY";
        }
        return r.dateSignature() != null ? "SIGNATORY" : "NO_ACTION";
    }

    /** 불일치 행 목록을 돌려준다. */
    public List<RatificationRow> findMismatches(List<RatificationRow> rows) {
        List<RatificationRow> bad = new ArrayList<>();
        for (RatificationRow r : rows) {
            String expected = derive(r);
            if (!expected.equals(r.status())) {
                bad.add(r);
                log.error("status 불일치: {} {} csv={} 날짜기준={}",
                        r.countryName(), r.treatyCode(), r.status(), expected);
            }
        }
        log.info("status 검증: 전체 {} / 불일치 {}", rows.size(), bad.size());
        return bad;
    }
}
