# Testing Guide

Run the app in mock mode before executing any of these scenarios:

**bash / macOS / Linux:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mock
```

**Windows PowerShell:**
```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=mock"
```

---

## H2 Console

| Field    | Value                    |
|----------|--------------------------|
| URL      | http://localhost:8080/h2-console |
| JDBC URL | `jdbc:h2:mem:jackpotdb`  |
| Username | `sa`                     |
| Password | *(leave empty)*          |

---

## Idempotency — same bet published twice

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"idem-test","userId":"user-1","jackpotId":"jackpot-fixed-fixed","amount":100.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"idem-test","userId":"user-1","jackpotId":"jackpot-fixed-fixed","amount":100.00}'
```

Verify in H2 console:
```sql
SELECT COUNT(*) FROM jackpot_contribution WHERE bet_id = 'idem-test';
-- Expected: 1
```

---

## Reward idempotency — evaluate twice

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"reward-test","userId":"user-1","jackpotId":"jackpot-always-win","amount":50.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/reward-test/evaluate" -Method POST -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8080/api/bets/reward-test/evaluate" -Method POST -UseBasicParsing
```

Expected: first evaluate returns `won:true`, second returns `409 REWARD_ALREADY_GIVEN`.

---

## Pool reset after win

After running the reward idempotency scenario above, verify in H2:
```sql
SELECT current_pool_amount, initial_pool_amount
FROM jackpot
WHERE id = 'jackpot-always-win';
-- Expected: current_pool_amount = initial_pool_amount
```

---

## Variable contribution decay

Place several bets on the variable jackpot:
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"var-1","userId":"user-1","jackpotId":"jackpot-variable-variable","amount":100.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"var-2","userId":"user-1","jackpotId":"jackpot-variable-variable","amount":100.00}'
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"var-3","userId":"user-1","jackpotId":"jackpot-variable-variable","amount":100.00}'
```

Verify in H2:
```sql
SELECT bet_id, contribution_amount, current_jackpot_amount
FROM jackpot_contribution
WHERE jackpot_id = 'jackpot-variable-variable'
ORDER BY created_at;
-- Expected: contribution_amount decreases with each row
```

---

## Input validation

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/bets" -Method POST -ContentType "application/json" -UseBasicParsing -Body '{"betId":"","userId":"","jackpotId":"","amount":-1}'
```

Expected: `400 {"code":"VALIDATION_ERROR","message":"..."}`
