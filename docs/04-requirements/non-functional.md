# Non-Functional Requirements (NFR) - Salud Activa

This document defines the quality, performance, and security standards that the Salud Activa system must meet to ensure professional operation.

---

## NFR-001: Performance

| Attribute | Metric | Test condition |
|-----------|--------|---------------|
| P95 Latency — Critical Endpoints | < 300ms | Under 100 RPS load |
| P99 Latency — Critical Endpoints | < 500ms | Under 200 RPS load |
| P95 Latency — Android App (Cold Start) | < 2s | Mid-range device (4GB RAM) |
| Service Startup Time (Backend) | < 20 seconds | AWS Fargate cold start |

**Defined Critical Endpoints:**
- `POST /api/turnos`: Critical to avoid double booking and ensure smooth UX.
- `POST /api/auth/login`: Critical for immediate platform access.

**Testing Tools:**
- k6 for the backend, Android Profiler for the mobile app.

---

## NFR-002: Availability

| Environment | SLO | Maintenance window | Max downtime/month |
|------------|-----|-------------------|-------------------|
| Production | 99.9% | Sundays 2am-4am (GMT-5) | 44 minutes |
| Staging | 95% | No restriction | 36 hours |

**Implemented Health Checks:**
- `GET /api/status`: Liveness and Readiness (validates connection to MongoDB Atlas and AWS RDS).

---

## NFR-003: Scalability

| Scenario | Expected behavior |
|---------|------------------|
| Gradual Growth | AWS ECS Auto-scaling activated when CPU > 70% or RAM > 75%. |
| Sudden Spikes | The system must scale new tasks in < 3 minutes. |
| Load Reduction | Automatic scale-down for cost optimization in AWS. |

**Strategy:** Stateless horizontal scaling. Session state is managed via JWT and database persistence.

---

## NFR-004: Security

### Authentication and Authorization
- Mandatory use of **JWT** in the `Authorization: Bearer <token>` header.
- Tokens expire in **1 hour**.
- Implementation of **Biometrics** in the Android App for local access.

### Data Transmission
- HTTPS mandatory in production via TLS 1.2+ (Managed by AWS ALB).
- Sensitive data (PII) encrypted at rest.

### Android Security
- Use of `EncryptedSharedPreferences` to store tokens and sensitive user data locally.
- Code obfuscation using R8/ProGuard in the production build.

---

## NFR-005: Observability

| Pillar | Requirement | Tool |
|--------|------------|------|
| Logs | Structured JSON format | Winston (Backend) / Logcat (Android) |
| Metrics | Error rate and duration per endpoint | CloudWatch Metrics (AWS) |
| Alerts | Notification in < 5 min via Email/Slack | CloudWatch Alarms |

---

## NFR-006: Maintainability

| Metric | Target |
|--------|--------|
| Test Coverage | ≥ 80% in business logic (ViewModels and Repositories). |
| Cyclomatic Complexity | ≤ 10 per function. |
| Onboarding | A new developer must be able to run `npm start` or the app in < 1 hour. |
| CI Build Time | < 6 minutes (GitHub Actions). |

---

## NFR-007: Disaster Recovery (DR)

| Scenario | RTO (Recovery Time Objective) | RPO (Recovery Point Objective) |
|---------|------------------------------|-------------------------------|
| Backend Instance Failure | < 1 minute (ECS Auto-restart) | 0 (Stateless) |
| DB Failure (RDS/Atlas) | < 5 minutes (Failover to replica) | < 5 seconds (Continuous backup) |
| AWS Zone Loss | < 15 minutes (Multi-AZ deployment) | < 1 minute |

---

## Priority Matrix

| NFR | Priority | Validated in CI? | Owner |
|-----|-----------|-----------------|-------------|
| Performance | P1 | Yes (k6) | Tech Lead |
| Availability | P1 | Yes | DevOps |
| Security | P1 | Yes (SonarQube) | Security |
| Maintainability | P2 | Yes (Jacoco/Jest) | Dev Team |