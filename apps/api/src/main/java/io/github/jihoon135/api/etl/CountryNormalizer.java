package io.github.jihoon135.api.etl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/** 국가명 → ISO3. 1) JDK 영문 표시명 정확 일치 2) alias.json 3) exclude 4) 나머지는 실패. */
@Component
public class CountryNormalizer {

    private static final Logger log = LoggerFactory.getLogger(CountryNormalizer.class);

    /** normalize 결과 집계. mapping 은 제외·실패를 뺀 이름→ISO3. */
    public record Result(
            Map<String, String> mapping,
            Map<String, String> auto,
            Map<String, String> alias,
            Map<String, String> excluded,
            List<String> failed) {
    }

    public Result normalize(Set<String> names, Path aliasFile) throws IOException {
        JsonNode root = JsonMapper.builder().build().readTree(aliasFile.toFile());
        Map<String, String> aliasMap = toMap(root.get("alias"));
        Map<String, String> excludeMap = toMap(root.get("exclude"));
        Map<String, String> jdkByName = jdkNameToIso3();

        Map<String, String> mapping = new TreeMap<>();
        Map<String, String> auto = new TreeMap<>();
        Map<String, String> alias = new TreeMap<>();
        Map<String, String> excluded = new TreeMap<>();
        List<String> failed = new ArrayList<>();

        for (String name : new java.util.TreeSet<>(names)) {
            String autoCode = jdkByName.get(name.toLowerCase(Locale.ROOT));
            String aliasCode = aliasMap.get(name);
            if (excludeMap.containsKey(name)) {
                excluded.put(name, excludeMap.get(name));
                log.info("제외: {} - {}", name, excludeMap.get(name));
            } else if (autoCode != null) {
                if (aliasCode != null && !aliasCode.equals(autoCode)) {
                    log.warn("alias 와 자동 매칭 불일치: {} alias={} auto={} (자동 매칭 채택)", name, aliasCode, autoCode);
                }
                mapping.put(name, autoCode);
                auto.put(name, autoCode);
            } else if (aliasCode != null) {
                mapping.put(name, aliasCode);
                alias.put(name, aliasCode);
            } else {
                failed.add(name);
            }
        }
        log.info("normalize 집계: 자동 {} / alias {} / 제외 {} / 실패 {}",
                auto.size(), alias.size(), excluded.size(), failed.size());
        if (!failed.isEmpty()) {
            log.error("매칭 실패 목록: {}", failed);
        }
        return new Result(mapping, auto, alias, excluded, failed);
    }

    private static Map<String, String> jdkNameToIso3() {
        Map<String, String> m = new HashMap<>();
        for (String iso2 : Locale.getISOCountries()) {
            Locale l = Locale.of("", iso2);
            m.put(l.getDisplayCountry(Locale.ENGLISH).toLowerCase(Locale.ROOT), l.getISO3Country());
        }
        return m;
    }

    private static Map<String, String> toMap(JsonNode node) {
        Map<String, String> m = new HashMap<>();
        node.properties().forEach(e -> m.put(e.getKey(), e.getValue().asString()));
        return m;
    }
}
