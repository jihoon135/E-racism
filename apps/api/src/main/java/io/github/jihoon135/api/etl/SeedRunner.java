package io.github.jihoon135.api.etl;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

/** seed 프로필 전용 ETL 진입점. 예외를 던지면 Boot 가 non-zero 로 종료한다. */
@Component
@Profile("seed")
public class SeedRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(SeedRunner.class);

    private final RatificationCsvReader csvReader;
    private final CountryNormalizer normalizer;
    private final StatusVerifier statusVerifier;
    private final SeedLoader loader;
    private final Path seedDir;

    public SeedRunner(RatificationCsvReader csvReader, CountryNormalizer normalizer,
                      StatusVerifier statusVerifier, SeedLoader loader,
                      @Value("${seed.dir}") Path seedDir) {
        this.csvReader = csvReader;
        this.normalizer = normalizer;
        this.statusVerifier = statusVerifier;
        this.loader = loader;
        this.seedDir = seedDir;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("seed 시작: dir={}", seedDir.toAbsolutePath().normalize());

        JsonMapper mapper = JsonMapper.builder().build();
        List<TreatySeed> treaties = mapper.convertValue(
                mapper.readTree(seedDir.resolve("treaties.json").toFile()).get("treaties"),
                new TypeReference<List<TreatySeed>>() { });
        List<RatificationRow> all = csvReader.read(seedDir.resolve("ratifications_6treaties.csv"));
        Set<String> treatyCodes = treaties.stream().map(TreatySeed::code).collect(Collectors.toSet());
        for (RatificationRow r : all) {
            if (!treatyCodes.contains(r.treatyCode())) {
                throw new IllegalStateException("treaties.json 에 없는 조약 코드: " + r.treatyCode());
            }
        }

        CountryNormalizer.Result n = normalizer.normalize(
                all.stream().map(RatificationRow::countryName).collect(Collectors.toSet()),
                seedDir.resolve("country-alias.json"));
        List<RatificationRow> mismatches = statusVerifier.findMismatches(all);

        if (!n.failed().isEmpty()) {
            throw new IllegalStateException("국가명 매칭 실패 " + n.failed().size() + "건, 적재 중단: " + n.failed());
        }
        if (!mismatches.isEmpty()) {
            throw new IllegalStateException("status 불일치 " + mismatches.size() + "건, 적재 중단");
        }

        // filter: 제외 대상(EU 등) 행 제거
        List<RatificationRow> rows = all.stream()
                .filter(r -> n.mapping().containsKey(r.countryName())).toList();
        Map<String, String> countries = new TreeMap<>();
        n.mapping().forEach((name, iso3) -> countries.put(iso3, name));
        if (countries.size() != n.mapping().size()) {
            throw new IllegalStateException("서로 다른 국가명이 같은 ISO3 로 매핑됨");
        }

        loader.load(treaties, countries, rows, n.mapping());
        log.info("seed 완료: treaty {} / country {} / ratification {} (제외된 행 {})",
                treaties.size(), countries.size(), rows.size(), all.size() - rows.size());
    }
}
