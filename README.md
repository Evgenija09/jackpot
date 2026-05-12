
# Jackpot Service

Backend service managing jackpot pool contributions and reward evaluation for a sports betting platform. Built as a Sporty Group technical assessment.

Bets are accepted via REST and published to Kafka. A Kafka consumer processes each bet asynchronously, contributing to the matching jackpot pool. Reward eligibility is evaluated on demand via a separate REST endpoint. Two pluggable strategy types — fixed and variable — control both contribution rates and reward chances.

---

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker (optional — only required for full Kafka stack)

---

## Getting Started

Clone the repository and navigate to the project directory:

```bash
git clone <repository-url>
cd jackpot
```

Replace `<repository-url>` with the GitHub repository URL.

---

## Running Options

Three supported modes. Choose based on your environment.

### Option 1 — Mock mode (recommended, no infrastructure needed)

The Kafka producer is replaced with a direct in-process call. The full bet → contribution → evaluate flow works immediately with no external services.

**bash / macOS / Linux:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mock
```

**Windows PowerShell:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=mock"
```

### Option 2 — Full stack with Kafka (Docker required)

Build the jar first, then start all services:

```bash
mvn clean package -DskipTests
docker-compose up --build
```

The app service is built from the local jar.
Run `mvn clean package -DskipTests` any time you make code changes before running docker-compose.

### Option 3 — Tests only

No infrastructure required. All 33 tests pass with an embedded H2 database.

```bash
mvn test
```

---

## H2 Console

Inspect the database while the application is running.

| Field    | Value                    |
|----------|--------------------------|
| URL      | http://localhost:8080/h2-console |
| JDBC URL | `jdbc:h2:mem:jackpotdb`  |
| Username | `sa`                     |
| Password | *(leave empty)*          |

---

## Seeded Jackpots

Four jackpots are available immediately on startup via `data.sql`.

| ID | Contribution | Reward |
|----|-------------|--------|
| `jackpot-fixed-fixed` | 10% fixed rate | 50% flat chance |
| `jackpot-variable-variable` | Variable, decays as pool grows | Variable, grows to 100% at pool 10000 |
| `jackpot-fixed-variable` | 5% fixed rate | Variable, hits 100% at pool 5000 |
| `jackpot-always-win` | 10% fixed rate | Hits 100% immediately — pool starts at limit. Use this for guaranteed win testing. |

---

## API Reference

### POST /api/bets — Publish a bet

Publishes a bet to Kafka (or processes directly in mock mode). Always returns 202 immediately.

**Request:**
```json
{
  "betId": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "user-1",
  "jackpotId": "jackpot-fixed-fixed",
  "amount": 100.00
}
```

**Response 202:**
```json
{
  "betId": "550e8400-e29b-41d4-a716-446655440000",
  "status": "ACCEPTED"
}
```

**Response 400 — validation failure:**
```json
{
  "code": "VALIDATION_ERROR",
  "message": "betId: must not be blank, amount: must be greater than or equal to 0.01"
}
```

---

### POST /api/bets/{betId}/evaluate — Evaluate reward

Checks whether the bet wins a jackpot reward. If won, the reward is persisted and the jackpot pool resets to its initial value.

**In mock mode:** call immediately after publishing — contribution is processed synchronously.

**In full Kafka mode:** wait 1–2 seconds for the consumer to process before evaluating.

**Response 200 — not won:**
```json
{
  "betId": "550e8400-e29b-41d4-a716-446655440000",
  "jackpotId": "jackpot-fixed-fixed",
  "won": false,
  "rewardAmount": null,
  "message": "Better luck next time!"
}
```

**Response 200 — won:**
```json
{
  "betId": "550e8400-e29b-41d4-a716-446655440000",
  "jackpotId": "jackpot-always-win",
  "won": true,
  "rewardAmount": 105.0000,
  "message": "Congratulations! You won the jackpot!"
}
```

**Response 404 — bet not yet processed:**
```json
{
  "code": "BET_NOT_YET_PROCESSED",
  "message": "Bet not yet processed: 550e8400-e29b-41d4-a716-446655440000"
}
```

**Response 409 — already rewarded:**
```json
{
  "code": "REWARD_ALREADY_GIVEN",
  "message": "Reward already given for bet: 550e8400-e29b-41d4-a716-446655440000"
}
```

---

### POST /api/jackpots — Create a jackpot

This endpoint is included for demo/testing purposes.

Creates a new jackpot with a custom strategy configuration.

**Request — fixed contribution, fixed reward:**
```json
{
  "jackpotId": "my-jackpot",
  "initialPoolAmount": 1000.00,
  "contributionStrategyType": "FIXED",
  "contributionRate": 0.10,
  "rewardStrategyType": "FIXED",
  "rewardChance": 0.05
}
```

**Request — variable contribution, variable reward:**
```json
{
  "jackpotId": "my-variable-jackpot",
  "initialPoolAmount": 1000.00,
  "contributionStrategyType": "VARIABLE",
  "contributionRate": 0.10,
  "contributionDecayRate": 0.00001,
  "rewardStrategyType": "VARIABLE",
  "rewardChance": 0.01,
  "rewardGrowthRate": 0.00001,
  "rewardPoolLimit": 10000.00
}
```

**Response 201:**
```json
{
  "id": "my-jackpot",
  "initialPoolAmount": 1000.00,
  "currentPoolAmount": 1000.00,
  "contributionStrategyType": "FIXED",
  "contributionRate": 0.10,
  "version": 0
}
```

---

## Example Flow

### Mock mode — full end-to-end (PowerShell)

```powershell
# Step 1 — Start the app in mock mode
mvn spring-boot:run "-Dspring-boot.run.profiles=mock"

# Step 2 — Publish a bet to the always-win jackpot
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" `
  -Method POST `
  -ContentType "application/json" `
  -UseBasicParsing `
  -Body '{"betId":"demo-001","userId":"user-1","jackpotId":"jackpot-always-win","amount":50.00}'

# Step 3 — Evaluate immediately (mock mode is synchronous)
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/demo-001/evaluate" `
  -Method POST `
  -UseBasicParsing

# Step 4 — Try evaluating again — expect 409 REWARD_ALREADY_GIVEN
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/demo-001/evaluate" `
  -Method POST `
  -UseBasicParsing
```

### curl (bash / macOS / Linux)

```bash
# Publish
curl -X POST http://localhost:8080/api/bets \
  -H "Content-Type: application/json" \
  -d '{"betId":"demo-001","userId":"user-1","jackpotId":"jackpot-always-win","amount":50.00}'

# Evaluate
curl -X POST http://localhost:8080/api/bets/demo-001/evaluate
```

---

## Key Test Scenarios

These scenarios verify the critical behaviours of the service.
Run the app in mock mode first:
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=mock"
```

### Idempotency — same bet published twice
Publish the same betId twice. Only one contribution row is created.
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"idem-test","userId":"user-1","jackpotId":"jackpot-fixed-fixed","amount":100.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"idem-test","userId":"user-1","jackpotId":"jackpot-fixed-fixed","amount":100.00}'
```
Verify in H2 console:
```sql
SELECT COUNT(*) FROM jackpot_contribution WHERE bet_id = 'idem-test';
-- Expected: 1
```

### Reward idempotency — evaluate twice
Evaluate a winning bet twice. Second call returns 409.
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"reward-test","userId":"user-1","jackpotId":"jackpot-always-win","amount":50.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/reward-test/evaluate" -Method POST -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/reward-test/evaluate" -Method POST -UseBasicParsing
```
Expected: first evaluate returns `won:true`, second returns `409 REWARD_ALREADY_GIVEN`.

### Pool reset after win
After a win, verify the jackpot pool resets to its initial value.
```sql
SELECT current_pool_amount, initial_pool_amount 
FROM jackpot 
WHERE id = 'jackpot-always-win';
-- Expected: current_pool_amount = initial_pool_amount
```

### Variable contribution decay
Multiple bets on the variable jackpot show decreasing contribution amounts as the pool grows.
```sql
SELECT bet_id, contribution_amount, current_jackpot_amount 
FROM jackpot_contribution 
WHERE jackpot_id = 'jackpot-variable-variable' 
ORDER BY created_at;
-- Expected: contribution_amount decreases with each row
```

### Input validation
Send an invalid request — expect 400 VALIDATION_ERROR.
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"","userId":"","jackpotId":"","amount":-1}'
```
Expected: `400 {"code":"VALIDATION_ERROR","message":"..."}`

---

## Kafka Retry and Dead Letter Topic

The Kafka consumer is configured with resilience for transient failures.

**Retry policy:**
- 3 attempts with exponential backoff: 1s → 2s → 4s
- `DuplicateBetException` and `JackpotNotFoundException` are classified as non-retryable — they are permanent failures and are acknowledged immediately without retry

**Dead Letter Topic:**
- After exhausting retries, messages are parked in `jackpot-bets.DLT`
- No message is ever silently lost

**Verifying DLT (requires docker-compose):**
```bash
# Inspect dead letter messages
docker exec -it <kafka-container-name> \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic jackpot-bets.DLT \
  --from-beginning
```

---

## Architecture Decisions

### Layered Architecture

Four layers: `api` → `domain` → `infrastructure` → `exception`.

The domain layer has zero infrastructure imports. Services, strategies, and domain models have no knowledge of MyBatis, Kafka, Spring MVC, or any framework. This means domain logic can be unit tested with zero infrastructure — no database, no broker, no HTTP server required.

### Two-Layer Idempotency

The same `betId` processed N times produces exactly one `JackpotContribution` row.

1. **Application pre-check:** `existsByBetId` before insert — fast path, avoids insert overhead
2. **UNIQUE constraint on `bet_id`:** DB-level safety net catches any race between the check and the insert

`DataIntegrityViolationException` is caught in the consumer and acknowledged silently — the duplicate is discarded without retry.

### Concurrency Safety

**Contribution processing — optimistic locking:**
```sql
UPDATE jackpot 
SET current_pool_amount = ?, version = version + 1 
WHERE id = ? AND version = ?
```
Zero rows updated means a concurrent thread won the race. The consumer throws `OptimisticLockException` and Kafka retries with exponential backoff.

**Reward evaluation — pessimistic locking:**
```sql
SELECT * FROM jackpot WHERE id = ? FOR UPDATE
```
The row is locked for the duration of the evaluate transaction. Two concurrent evaluate calls for the same jackpot are serialised — one winner per pool cycle is guaranteed.

The two strategies are intentional: contributions are high-frequency and benefit from the throughput of optimistic locking; reward evaluation is low-frequency and high-stakes, warranting the stronger guarantee of pessimistic locking.

### MyBatis over JPA

Explicit SQL in XML mapper files. Every query is visible and auditable in `src/main/resources/mapper/`. No N+1 risk by design — there is no lazy loading. Decimal precision is enforced at the SQL layer (`DECIMAL(19,8)` for rate columns). Matches Sporty's production stack.

---

## Variable Formula Definitions

The spec defined the desired behaviour but not the formula. Linear functions were chosen for simplicity and predictability.

**Contribution decay:**
```
effectiveRate = max(baseRate − (decayRate × currentPoolAmount), 0.01)
contribution  = betAmount × effectiveRate
```

**Reward growth:**
```
effectiveChance = min(baseChance + (growthRate × currentPoolAmount), 1.0)
```

The floor of 0.01 (1%) on contribution ensures the jackpot always grows, regardless of pool size. The cap of 1.0 (100%) on reward chance ensures the variable strategy converges to a guaranteed win at the pool limit.

---

## Assumptions

1. **Jackpots are pre-seeded.** In production, an external admin service would own jackpot lifecycle. Seeding via `data.sql` and exposing `POST /api/jackpots` is appropriate for assessment scope.
2. **Reward evaluation is caller-triggered.** The consumer processes contributions only. A separate REST call evaluates reward eligibility. The consumer does not auto-evaluate.
3. **One winner per pool cycle.** Concurrent evaluate calls for the same jackpot are serialised via `SELECT FOR UPDATE`. After a win, the pool resets before the next cycle begins.
4. **`betId` is provided by the caller.** Client-generated IDs are required for idempotent retry semantics — the server cannot regenerate the same ID on retry.
5. **Variable formulas use linear functions.** Defined and documented as reasonable defaults.

---

## Production Considerations

- For larger deployments, **jackpot-level locking** could be moved to a distributed coordination mechanism or enforced through partitioning by jackpotId.
- **Kafka topology:** Cluster with multiple brokers, topic partitioned by `jackpotId` to preserve per-jackpot ordering
- **Caching:** Jackpot configuration in Redis — frequent reads, rare writes
- **Observability:** Micrometer + Prometheus, structured JSON logging with correlation IDs, distributed tracing
- **DLT consumer:** Dedicated service for inspecting and replaying dead letter messages

---

## AI Use

Claude Code was used to generate implementation from prompts I wrote after reading the spec and identifying invariants, ambiguities, and design decisions independently. I reviewed output after each prompt, caught where it took shortcuts, and corrected those manually. Test cases were designed by me and handed to Claude for implementation. All architecture decisions, final validation, and code correctness are my own.