# Equalizer (가칭)

전 세계 반차별·인권 국제 조약의 참여·이행 현황을 **3D 인터랙티브 지구본**에서 탐색하고,
**RAG 기반 AI**로 공식 출처에 근거한 설명을 받는 풀스택 웹 플랫폼.

> 🚧 개발 중 — 현재 **Phase 1 (로컬 관통)**

---

## 문서

설계 배경, 데이터 출처와 한계, 결정 기록은 `docs/`에 있다.

- [`docs/00-배경.md`](docs/00-배경.md) — 이 프로젝트가 무엇을, 왜, 어떤 원칙으로 만드는지
- [`docs/01-데이터.md`](docs/01-데이터.md) — 데이터를 어디서 가져왔고 무엇을 알 수 없는지 (출처·검증·한계·표기 원칙) ⭐
- [`docs/02-아키텍처.md`](docs/02-아키텍처.md) — 기술 스택, 시스템 구조, DB 스키마
- [`docs/03-계획.md`](docs/03-계획.md) — Phase 0~6 로드맵
- [`docs/decisions/`](docs/decisions/) — 설계 결정과 그 이유의 기록

## 데이터

- `data/seed/ratifications_6treaties.csv` — 6개 조약 × 198개 주체 = 1,188행
- `data/seed/country-alias.json` — UN 공식 표기 → ISO3 매핑 / 제외 목록
- `data/raw/` — 내려받은 원본 (gitignore)

출처: [OHCHR Status of Ratification Dashboard](https://indicators.ohchr.org/) ·
원 출처: UN 법무실(OLA) 데이터베이스

## 이 프로젝트의 원칙

1. **AI는 판단자가 아니라 설명자다** — 공식 문서 범위 안에서만 답한다
2. **"차별 지도"가 아니라 "조약 참여·이행 현황"이다** — 국가를 평가하지 않는다
3. **자료가 없으면 없다고 말한다** — 빈 상태는 결함이 아니라 정체성이다
4. **출처는 원출처 + 경유처를 함께** — 발화 주체를 명시한다

## 실행

필요한 것: Docker, JDK 21. (Gradle 은 `apps/api/gradlew` 가 받아온다.)

```bash
cp .env.example .env                # 로컬 개발용 DB 설정
docker compose up -d                # postgres + pgvector
./scripts/seed.sh                   # ETL: CSV → ISO3 매핑 → DB (여러 번 실행해도 된다)
cd apps/api && ./gradlew bootRun    # API 서버, http://localhost:8080
```

API 가 떠 있는 동안 다른 터미널에서:

```bash
curl http://localhost:8080/api/treaties/ICERD/ratifications
```

조약 코드는 `ICERD`, `ICCPR`, `CEDAW`, `CRC`, `CRPD`, `ICRMW`. 알 수 없는 코드는 404 를 돌려준다.

- OpenAPI 명세: <http://localhost:8080/v3/api-docs>
- Swagger UI: <http://localhost:8080/swagger-ui.html>

`apps/web` (지구본 화면) 은 준비 중이다. Phase 1-5 에서 만든다.
