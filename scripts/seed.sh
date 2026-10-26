#!/usr/bin/env bash
# 시드 ETL 실행. 추가 인자는 bootRun 인자 뒤에 붙는다 (예: --seed.dir=/tmp/x)
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT/apps/api"
./gradlew bootRun --args="--spring.profiles.active=seed $*"
