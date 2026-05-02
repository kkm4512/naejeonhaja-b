# 내전하자 (Naejeonhaja) - 백엔드

리그 오브 레전드 팀 밸런싱 API 서버입니다. 10명의 플레이어를 최적으로 균형 잡힌 2개 팀으로 나누고, Riot API를 통해 플레이어 정보를 조회합니다.

## 📋 프로젝트 정보

- **프레임워크**: Spring Boot 3.3.1
- **Java 버전**: Java 17
- **포트**: 8080
- **API 문서**: [Swagger UI](http://localhost:8080/docs)

## 🔧 기술 스택

- Spring Boot Web
- Lombok
- Jakarta Validation
- Springdoc OpenAPI (Swagger)
- RestTemplate
- Jackson (JSON 처리)

## 🚀 시작하기

### 1. 환경 설정

`.env` 파일을 프로젝트 루트에 생성하고 Riot API 키를 추가합니다:

```env
RIOT_API_KEY=your-riot-api-key-here
```

### 2. 빌드 및 실행

#### 로컬 개발 환경
```bash
# 빌드
./gradlew build

# 실행
./gradlew bootRun
```

#### Docker 환경
```bash
# Docker Compose로 빌드 및 실행 (권장)
docker-compose up --build

# 또는 Docker만으로 실행
docker build -t naejeonhaja .
docker run -p 8080:8080 --env-file .env naejeonhaja

# 백그라운드 실행
docker-compose up -d --build
```

#### Docker 명령어
```bash
# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs -f naejeonhaja

# 컨테이너 중지
docker-compose down

# 컨테이너 재시작
docker-compose restart
```

### 3. API 문서 확인

서버 시작 후 [http://localhost:8080/docs](http://localhost:8080/docs)에서 Swagger UI를 통해 모든 API를 확인할 수 있습니다.

---

## 📡 API 엔드포인트

### 1. LOL 팀 밸런싱 API

#### 1-1. Rift 팀 생성 (10vs10)
**엔드포인트**: `POST /api/v1/lol/rift/teams`

플레이어 10명의 정보를 기반으로 최적의 밸런스를 가진 2개 팀을 생성합니다.

**요청 예시**:
```json
{
  "lolPlayers": [
    {
      "name": "Player1",
      "tier": "GOLD_I",
      "lines": [
        {
          "line": "TOP",
          "lineRole": "MAINLINE"
        },
        {
          "line": "JUNGLE",
          "lineRole": "SUBLINE"
        }
      ],
      "mmr": 0,
      "mmrReduced": false
    },
    {
      "name": "Player2",
      "tier": "SILVER_II",
      "lines": [
        {
          "line": "MID",
          "lineRole": "MAINLINE"
        }
      ],
      "mmr": 0,
      "mmrReduced": true
    }
    // ... 8명 추가 (총 10명)
  ]
}
```

**필수 필드**:
- `lolPlayers`: 정확히 10명의 플레이어 배열
  - `name`: 플레이어 닉네임
  - `tier`: 플레이어 티어 (IRON_IV ~ CHALLENGER_I)
  - `lines`: 플레이어가 할 수 있는 라인 배열
    - `line`: TOP, JUNGLE, MID, AD, SUPPORT
    - `lineRole`: MAINLINE (주 라인), SUBLINE (부 라인)
  - `mmr`: MMR 값 (0이면 티어에 따라 자동 설정)
  - `mmrReduced`: 서브 라인 선택 시 MMR 감소 여부

**응답 예시**:
```json
{
  "code": 200,
  "message": "요청이 성공했습니다.",
  "data": {
    "balance": "EXCELLENT",
    "teamATotalMmr": 5500,
    "teamBTotalMmr": 5450,
    "teamA": [
      {
        "name": "Player10",
        "tier": "CHALLENGER_I",
        "lines": [
          {
            "line": "TOP",
            "lineRole": "MAINLINE"
          }
        ],
        "mmr": 2200,
        "mmrReduced": false
      },
      // ... 4명 추가
    ],
    "teamB": [
      // ... 5명
    ]
  }
}
```

**밸런스 평가 기준**:
- `PERFECT` (0-100): 완벽한 밸런스
- `EXCELLENT` (101-300): 우수한 밸런스
- `GOOD` (301-600): 좋은 밸런스
- `FAIR` (601-1000): 보통 밸런스
- `POOR` (1001+): 불균형한 밸런스

#### 1-2. Abyss 팀 생성
**엔드포인트**: `POST /api/v1/lol/abyss/team`

Rift와 동일한 요청/응답 형식입니다.

---

### 2. Riot API 연동

#### 2-1. 플레이어 상세 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/player/{playerName}`

플레이어의 상세 정보 (계정, 소환사, 랭크, 챔피언 마스터리, 챔피언 정보)를 조회합니다.

**경로 매개변수**:
- `playerName`: 플레이어 이름 (예: `PlayerName#KR`)
  - ⚠️ **주의**: `#` 문자는 URL 인코딩으로 `%23`으로 변환해야 합니다
  - 예: `자신있게싸우자#4444` → `자신있게싸우자%234444`

**응답**:
```json
{
  "code": 200,
  "message": "요청이 성공했습니다.",
  "data": {
    "account": { /* 계정 정보 */ },
    "summoner": { /* 소환사 정보 */ },
    "masteries": [ /* 상위 3개 챔피언 마스터리 */ ],
    "league": { /* 랭크 정보 */ },
    "champions": [ /* 마스터리 챔피언 정보 */ ]
  }
}
```

#### 2-2. 플레이어 기본 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/player/{playerName}/basic`

플레이어의 기본 정보 (계정, 소환사, 랭크)만 조회합니다. 상세 조회보다 빠릅니다.

#### 2-3. 계정 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/account/{playerName}`

플레이어의 계정 정보를 직접 조회합니다.

#### 2-4. 소환사 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/summoner/{puuid}`

PUUID를 통해 소환사 정보를 조회합니다.

**경로 매개변수**:
- `puuid`: 플레이어 UUID

#### 2-5. 랭크 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/league/{puuid}`

PUUID를 통해 랭크 정보(Solo Ranked 기준)를 조회합니다.

#### 2-6. 챔피언 마스터리 조회
**엔드포인트**: `GET /api/v1/game/lol/riot/champion-mastery/{puuid}`

플레이어의 상위 3개 챔피언 마스터리 정보를 조회합니다.

---

### 3. DataDragon API

#### 3-1. 챔피언 정보 조회
**엔드포인트**: `GET /api/v1/game/lol/dataDragon/champion/{championId}`

특정 챔피언의 상세 정보를 조회합니다.

**경로 매개변수**:
- `championId`: 챔피언 ID (숫자)

**응답**:
```json
{
  "code": 200,
  "message": "요청이 성공했습니다.",
  "data": {
    "id": "Ahri",
    "key": "103",
    "name": "Ahri",
    "title": "Nine-Tailed Fox",
    "blurb": "/* 챔피언 설명 */",
    // ... 더 많은 정보
  }
}
```

#### 3-2. 챔피언 정보 재로드
**엔드포인트**: `POST /api/v1/game/lol/dataDragon/reload`

모든 챔피언 정보를 Riot DataDragon API에서 재로드합니다. 새 챔피언이 추가되거나 정보 업데이트가 필요할 때 사용합니다.

**응답**:
```json
{
  "code": 200,
  "message": "요청이 성공했습니다."
}
```

---

## 🎯 팀 밸런싱 알고리즘

### 동작 방식

1. **MMR 초기화**: 사용자가 제공한 MMR이 0이면 티어에 따라 자동 설정
2. **초기 팀 분할**: 플레이어들을 MMR 기준 내림차순 정렬 후 라운드로빈 방식으로 분할
3. **브루트포스 최적화**: 모든 가능한 조합(최대 1,024개)을 시도하여 MMR 차이가 가장 작은 팀 구성 선택
4. **라인 정규화**: 각 팀의 라인을 정규화
5. **라인 할당**: 각 플레이어에게 최적의 라인 할당 (메인 라인 우선)
6. **라인 검증**: 각 팀의 TOP, JUNGLE, MID, AD, SUPPORT가 모두 배정되었는지 확인
7. **라인 정렬**: 최종 결과를 라인 순서에 따라 정렬

### 특징

- **완벽한 균형**: 각 팀마다 모든 라인(TOP, JUNGLE, MID, AD, SUPPORT)이 정확히 1명씩 배정
- **중복 없음**: 같은 라인을 할 사람이 중복되지 않음
- **MMR 밸런싱**: 팀 전체의 총 MMR 차이를 최소화하여 가장 공정한 매치업 생성
- **서브 라인 페널티**: 서브 라인 선택 시 MMR을 200 감소시켜 밸런싱 고려

---

## 🏆 티어별 기본 MMR

- IRON_IV: 700
- BRONZE_I: 900
- SILVER_II: 1100
- GOLD_I: 1200
- PLATINUM_III: 1400
- EMERALD_I: 1500
- DIAMOND_II: 1600
- MASTER_I: 1800
- GRANDMASTER_I: 2000
- CHALLENGER_I: 2200

---

## 📝 라인(Position) 정의

- **TOP**: 상단 라인
- **JUNGLE**: 정글 (맵 외부 지역)
- **MID**: 중앙 라인
- **AD**: ADC (Attack Damage Carry, 하단 라인 딜러)
- **SUPPORT**: 하단 라인 서포터

---

## ⚠️ 에러 핸들링

### 응답 코드

- `200`: 성공
- `400`: 잘못된 요청 (예: 10명이 아닌 플레이어 수)
- `404`: 플레이어를 찾을 수 없음 (Riot API)
- `500`: 서버 오류

### 실패 응답 예시

```json
{
  "code": 400,
  "message": "정확히 10명의 플레이어가 필요합니다."
}
```

---

## 🔐 환경 변수

| 변수명 | 설명 | 예시 |
|--------|------|------|
| `RIOT_API_KEY` | Riot API 키 (필수) | `RGAPI-xxx-xxx-xxx` |

---

## 🌐 CORS 설정

프론트엔드와의 상호작용을 위해 CORS(Cross-Origin Resource Sharing)가 설정되어 있습니다.

### 허용된 엔드포인트
- `/api/**`: 모든 API 엔드포인트
- `/health`: Health Check
- `/docs/**`: Swagger UI

### 허용된 메서드
- `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`

### 허용된 헤더
- 모든 헤더 허용

### 개발 환경
현재 모든 도메인(`*`)에서 접근 가능하도록 설정되어 있습니다.

### 프로덕션 환경
보안을 위해 특정 도메인만 허용하는 것을 권장합니다:

```java
.allowedOrigins("https://yourdomain.com", "https://www.yourdomain.com")
```

---

## 📚 프로젝트 구조

```
src/main/java/com/example/naejeonhaja/
├── domain/
│   └── game/
│       ├── lol/
│       │   ├── controller/
│       │   │   ├── RiftController.java    (Rift 팀 생성)
│       │   │   └── AbyssController.java   (Abyss 팀 생성)
│       │   ├── service/
│       │   │   ├── RiftService.java       (Rift 로직)
│       │   │   ├── AbyssService.java      (Abyss 로직)
│       │   │   ├── balancing/             (밸런싱 알고리즘)
│       │   │   └── util/
│       │   ├── dto/                        (데이터 전송 객체)
│       │   ├── enums/                      (Enum 정의)
│       │   └── repository/
│       ├── riot/
│       │   ├── controller/
│       │   ├── service/
│       │   └── dto/
│       └── dataDragon/
│           ├── controller/
│           └── service/
├── common/
│   ├── exception/             (예외 처리)
│   ├── response/              (공통 응답 형식)
│   ├── helper/                (유틸리티)
│   └── config/                (설정)
└── NaejeonhajaApplication.java
```

---

## 🧪 테스트 방법

### cURL 예시

```bash
# Rift 팀 생성
curl -X POST http://localhost:8080/api/v1/lol/rift/teams \
  -H "Content-Type: application/json" \
  -d '{
    "lolPlayers": [
      {"name":"Player1","tier":"GOLD_I","lines":[{"line":"TOP","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player2","tier":"SILVER_II","lines":[{"line":"MID","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player3","tier":"PLATINUM_III","lines":[{"line":"AD","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player4","tier":"BRONZE_I","lines":[{"line":"JUNGLE","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player5","tier":"DIAMOND_II","lines":[{"line":"SUPPORT","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player6","tier":"IRON_IV","lines":[{"line":"TOP","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player7","tier":"EMERALD_I","lines":[{"line":"MID","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player8","tier":"MASTER_I","lines":[{"line":"JUNGLE","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player9","tier":"GRANDMASTER_I","lines":[{"line":"SUPPORT","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false},
      {"name":"Player10","tier":"CHALLENGER_I","lines":[{"line":"AD","lineRole":"MAINLINE"}],"mmr":0,"mmrReduced":false}
    ]
  }'

# 플레이어 정보 조회 (# 문자는 %23으로 인코딩)
curl -X GET "http://localhost:8080/api/v1/game/lol/riot/player/PlayerName%23KR"

# 챔피언 정보 조회
curl -X GET "http://localhost:8080/api/v1/game/lol/dataDragon/champion/103"
```

---

## 📖 주요 서비스 설명

### RiftService & AbyssService
플레이어들을 최적으로 균형 잡힌 2개 팀으로 분할하는 메인 로직입니다.

### RiotService
Riot Official API와 통신하여 플레이어 정보를 조회합니다.

### DataDragonService
Riot DataDragon에서 챔피언 정보를 캐싱 및 조회합니다.

---

## 📞 지원

문제가 발생하거나 기능 요청이 있으시면 이슈를 등록해주세요.

---

## 🐳 Docker 배포

프로젝트는 Docker를 통한 컨테이너화 배포를 지원합니다.

### 파일 구조

```
├── Dockerfile              # Multi-stage build Dockerfile
├── Dockerfile.simple       # 간단한 Dockerfile (빌드된 JAR 필요)
├── docker-compose.yml      # Docker Compose 설정
└── .dockerignore          # Docker 빌드 시 제외할 파일들
```

### Docker Compose 사용 (권장)

```bash
# 빌드 및 실행
docker-compose up --build

# 백그라운드 실행
docker-compose up -d --build

# 로그 확인
docker-compose logs -f

# 중지
docker-compose down
```

### Docker 직접 사용

```bash
# Multi-stage build
docker build -t naejeonhaja .
docker run -p 8080:8080 --env-file .env naejeonhaja

# JAR 파일이 있는 경우 (Dockerfile.simple)
docker build -f Dockerfile.simple -t naejeonhaja-simple .
docker run -p 8080:8080 --env-file .env naejeonhaja-simple
```

### Health Check

컨테이너는 Spring Boot Actuator의 health 엔드포인트(`/actuator/health`)를 통해 상태를 모니터링합니다.

```bash
# Health check 직접 확인
curl http://localhost:8080/health
```

**응답**:
```json
{
  "status": "UP"
}
```

---

## 📊 모니터링

### Actuator 엔드포인트

- **Health**: `GET /actuator/health` - 애플리케이션 상태 확인
- **Info**: `GET /actuator/info` - 애플리케이션 정보 (비활성화)

### 로그 확인

```bash
# Docker Compose 로그
docker-compose logs -f naejeonhaja

# 특정 시간대 로그
docker-compose logs --since 1h naejeonhaja
```

---

**최종 수정**: 2026-04-29
**버전**: 0.0.1-SNAPSHOT
