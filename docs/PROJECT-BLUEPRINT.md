# Project Blueprint

## Feature Flag Management System

This document turns the SRS into a concrete implementation plan for building, shipping, and open-sourcing the project.

## 1. Product Goal

Build an open-source, self-hosted feature flag management platform that allows teams to:

- manage applications and environments
- create and update feature flags
- define rollout and targeting rules
- evaluate flags at runtime with low latency
- audit configuration changes
- integrate via REST APIs and SDKs

The core product is the evaluation engine. The dashboard and management APIs exist to safely feed configuration into that engine.

## 2. Recommended v1 Scope

The v1 release should include:

- admin authentication
- role-based access control
- application management
- environment management
- feature flag CRUD
- rule types:
  - global on/off
  - percentage rollout
  - user ID targeting
  - environment-specific rules
- deterministic evaluation API
- audit logs
- dashboard for management
- Redis-based config caching
- health checks, metrics, logs
- Java SDK
- Docker Compose-based local and self-hosted deployment

The v1 release should not include:

- multivariate experiments
- realtime streaming updates to SDKs
- advanced segmentation UI
- geo-based targeting
- time-window activation
- multi-region active-active deployment

## 3. Architecture

## 3.1 Logical Components

### `api`

Owns:

- authentication/session handling
- application/environment/flag CRUD
- rule validation
- audit log creation
- admin dashboard APIs

### `evaluator`

Owns:

- runtime flag evaluation endpoint
- deterministic rollout logic
- cache lookups
- fallback behavior
- evaluation metrics

In v1, `api` and `evaluator` can be implemented as one backend service with separate modules. Split them later only if scale requires it.

### `web`

Owns:

- dashboard UI
- login flow
- application and flag management screens
- search/filtering
- audit history view

### `postgres`

Source of truth for:

- users
- roles
- applications
- environments
- feature flags
- rules
- audit logs
- service credentials

### `redis`

Used for:

- cached flag configurations
- low-latency evaluation reads
- short-lived rate limit counters if needed

### `observability`

Use:

- Prometheus for metrics
- Grafana for dashboards
- Loki for logs

## 3.2 Deployment Model

### Recommended starting model

- Docker Compose for local development
- Coolify for self-hosted deployments
- GitHub Actions for CI
- Coolify auto-deploy for CD

### Later production model

- Kubernetes
- Argo CD
- managed Postgres/Redis or HA self-hosted equivalents

## 3.3 Request Flow

### Management flow

1. Admin authenticates.
2. Admin creates or updates flag config.
3. Backend validates and writes changes to Postgres.
4. Backend writes audit log entry.
5. Backend invalidates or refreshes Redis cache.
6. Future evaluations use updated config.

### Evaluation flow

1. Client SDK or app sends evaluation request.
2. Backend checks Redis for resolved flag config.
3. On cache miss, backend loads config from Postgres and populates Redis.
4. Evaluator applies rule priority and deterministic rollout logic.
5. Backend returns boolean result and logs metrics.
6. On failure or missing flag, backend returns safe default `false`.

## 4. Suggested Tech Stack

Pick one backend stack and stay consistent. Two good options:

### Option A: Java-first

- backend: Spring Boot
- auth: Spring Security or Keycloak integration
- database: PostgreSQL
- cache: Redis
- web: React
- SDK: Java

### Option B: TypeScript-first

- backend: NestJS
- auth: NestJS auth modules or Keycloak integration
- database: PostgreSQL
- cache: Redis
- web: React
- SDK: Java

If the goal is stronger alignment with the Java SDK and enterprise positioning, Option A is the better fit.

## 5. Repository Structure

```text
feature_flag_management_system/
├── apps/
│   ├── api/
│   ├── web/
│   └── java-sdk/
├── packages/
│   ├── domain/
│   ├── evaluator/
│   ├── api-contracts/
│   ├── ui-components/
│   └── test-utils/
├── infra/
│   ├── docker/
│   ├── compose/
│   ├── coolify/
│   ├── k8s/
│   └── scripts/
├── docs/
│   ├── SRS-v1.md
│   ├── PROJECT-BLUEPRINT.md
│   ├── ARCHITECTURE.md
│   ├── API.md
│   ├── DEPLOYMENT.md
│   └── ROADMAP.md
├── .github/
│   └── workflows/
├── examples/
│   ├── sample-app-java/
│   └── sample-app-rest/
├── CONTRIBUTING.md
├── CODE_OF_CONDUCT.md
├── SECURITY.md
├── LICENSE
└── README.md
```

## 6. Domain Model

## 6.1 Core Entities

### `users`

- id
- email
- password_hash or external_auth_id
- status
- created_at
- updated_at

### `roles`

- id
- name

Example values:

- `ADMIN`
- `DEVELOPER`
- `VIEWER`

### `user_roles`

- user_id
- role_id

### `applications`

- id
- key
- name
- description
- created_by
- created_at
- updated_at

### `environments`

- id
- application_id
- key
- name
- created_at
- updated_at

Examples:

- `dev`
- `staging`
- `prod`

### `feature_flags`

- id
- application_id
- environment_id
- key
- name
- description
- enabled
- archived
- created_by
- created_at
- updated_at

### `flag_rules`

- id
- feature_flag_id
- rule_type
- priority
- value_json
- created_at
- updated_at

Example `rule_type` values:

- `GLOBAL`
- `PERCENTAGE`
- `USER_TARGET`

### `audit_logs`

- id
- actor_user_id
- entity_type
- entity_id
- action
- previous_value_json
- new_value_json
- created_at

### `service_tokens`

- id
- application_id
- environment_id
- token_hash
- name
- last_used_at
- created_at
- revoked_at

## 6.2 Indexes You Should Plan For

- unique `(application_id, environment_id, key)` on `feature_flags`
- index on `applications.key`
- index on `(application_id, environment_id)` for flags
- index on `(entity_type, entity_id, created_at)` for audit logs
- index on `service_tokens.token_hash`

## 7. Rule Engine Design

Use a fixed rule evaluation order:

1. flag existence check
2. environment match
3. global disable
4. explicit user targeting allow/deny
5. percentage rollout
6. default fallback

Rules:

- Missing flag returns `false`
- Invalid rule config should never be saved
- Same user must get the same percentage rollout result every time
- Use a stable hash of `flagKey + userId` for rollout buckets

Recommended rollout implementation:

- hash input: `applicationKey:environmentKey:flagKey:userId`
- convert hash to an integer bucket from `0-99`
- return `true` if bucket is below rollout percentage

## 8. API Design

## 8.1 Management APIs

Suggested endpoints:

- `POST /api/v1/auth/login`
- `POST /api/v1/auth/logout`
- `GET /api/v1/applications`
- `POST /api/v1/applications`
- `PATCH /api/v1/applications/:id`
- `DELETE /api/v1/applications/:id`
- `GET /api/v1/applications/:id/environments`
- `POST /api/v1/applications/:id/environments`
- `GET /api/v1/flags`
- `POST /api/v1/flags`
- `GET /api/v1/flags/:id`
- `PATCH /api/v1/flags/:id`
- `DELETE /api/v1/flags/:id`
- `GET /api/v1/audit-logs`

## 8.2 Evaluation API

Suggested endpoint:

- `POST /api/v1/evaluate`

Request:

```json
{
  "applicationKey": "payments-service",
  "environmentKey": "prod",
  "flagKey": "new-checkout",
  "userId": "user-123",
  "attributes": {
    "plan": "pro"
  }
}
```

Response:

```json
{
  "enabled": true,
  "reason": "percentage_rollout",
  "flagKey": "new-checkout"
}
```

## 8.3 API Rules

- version the API from the start
- use consistent error shapes
- validate all inputs
- keep management and evaluation concerns separate
- require service tokens or API keys for evaluation

## 9. Security Plan

- protect admin routes with authentication
- enforce role-based access control
- store only hashed tokens
- rotate service tokens
- use HTTPS in deployed environments
- validate all request payloads
- add rate limiting on auth and evaluation endpoints
- avoid returning sensitive configuration details in public evaluation APIs
- maintain audit logs for all privileged changes

If you want an OSS identity provider, Keycloak is a good fit. If you want simpler v1 delivery, build in-app auth first and keep Keycloak as a future integration.

## 10. Caching Strategy

Use Redis to cache flag configurations by:

- application
- environment
- flag key

Suggested Redis key:

```text
ff:{applicationKey}:{environmentKey}:{flagKey}
```

Cache contents:

- flag metadata
- evaluated rule config snapshot
- updated timestamp or version

Invalidation strategy:

1. update Postgres
2. write audit log
3. delete or refresh affected Redis key
4. optionally publish an invalidation event later if you split services

## 11. Testing Strategy

## 11.1 Unit Tests

Must cover:

- rollout hashing determinism
- rule priority ordering
- missing flag fallback
- explicit user targeting
- invalid rule rejection

## 11.2 Integration Tests

Must cover:

- auth flows
- application/flag CRUD
- audit log generation
- cache refresh after updates
- evaluation endpoint using Postgres + Redis

## 11.3 API Contract Tests

Must cover:

- request validation
- error responses
- backward compatibility for public API contracts

## 11.4 Performance Tests

Must cover:

- evaluation latency under load
- cache hit behavior
- concurrent requests

## 11.5 Smoke Tests

Run after deploy:

- `/health`
- login flow
- flag creation
- flag evaluation

## 12. Observability Plan

Add:

- structured application logs
- Prometheus metrics
- Grafana dashboards
- Loki log aggregation

Track metrics such as:

- evaluation request count
- enabled response count
- disabled response count
- p50/p95/p99 latency
- cache hit rate
- auth failures
- error count by endpoint

## 13. Backup and Recovery

At minimum:

- scheduled Postgres backups
- documented restore process
- restore drill before public launch
- retention policy for audit and operational logs

If self-hosting with Coolify, keep deployment easy but treat data backup separately and explicitly.

## 14. CI/CD Blueprint

## 14.1 Recommended Pipeline

Use:

- GitHub Actions for CI
- Coolify for CD

### CI jobs

- formatting/lint
- unit tests
- integration tests
- migration validation
- Docker build
- dependency and security scans

### CD flow

1. PR opened
2. GitHub Actions runs checks
3. PR merged to `main`
4. image built and tagged
5. Coolify deploys
6. smoke tests run

## 14.2 Suggested Workflows

### `.github/workflows/ci.yml`

Runs on:

- pull requests
- pushes to `main`

Steps:

- install dependencies
- lint
- unit test
- integration test
- build

### `.github/workflows/release.yml`

Runs on:

- version tags like `v0.1.0`

Steps:

- build release image
- publish release notes
- attach artifacts if needed

### `.github/workflows/security.yml`

Runs:

- daily
- on dependency changes

Steps:

- dependency audit
- container image scan

## 15. Branching and Delivery Strategy

Use a simple branch model:

- `main` is always releasable
- feature branches for active work
- optional `release/*` branches only when the team becomes larger

Workflow:

1. create feature branch
2. push small commits
3. open PR early
4. require passing CI
5. merge with squash or rebase
6. auto-deploy `main`

## 16. Milestone Plan

## Milestone 0: Foundation

- choose stack
- initialize repo structure
- set up Docker Compose
- add Postgres and Redis
- add base backend and frontend apps
- set up CI

## Milestone 1: Core Data and CRUD

- database schema
- migrations
- application management
- environment management
- flag CRUD

Exit criteria:

- applications, environments, and flags can be managed through APIs

## Milestone 2: Evaluation Engine

- rule model
- deterministic rollout logic
- evaluation endpoint
- missing flag fallback

Exit criteria:

- runtime evaluation works correctly and deterministically

## Milestone 3: Caching and Auditability

- Redis cache
- cache invalidation
- audit logs
- audit filtering

Exit criteria:

- updates propagate correctly and changes are traceable

## Milestone 4: Security and Dashboard

- login/logout
- RBAC
- dashboard screens
- search and filtering

Exit criteria:

- authorized users can safely manage flags from the UI

## Milestone 5: SDK and Operability

- Java SDK
- metrics
- logs
- health checks
- backup scripts/docs

Exit criteria:

- external apps can integrate safely and operators can run the system

## Milestone 6: OSS Release Readiness

- README
- contribution guide
- code of conduct
- security policy
- install docs
- roadmap
- example applications

Exit criteria:

- a new contributor can clone, run, and understand the project

## 17. Open-Source Packaging

Before making the repository public, add:

- `README.md`
- `LICENSE`
- `CONTRIBUTING.md`
- `CODE_OF_CONDUCT.md`
- `SECURITY.md`
- `docs/ROADMAP.md`
- issue templates
- PR template

Recommended license:

- `Apache-2.0` if you want strong contributor and commercial clarity
- `MIT` if you want the simplest permissive license

## 18. Immediate Next Steps

The first implementation sprint should produce:

1. repo skeleton
2. local `docker-compose.yml`
3. backend service bootstrap
4. frontend service bootstrap
5. Postgres schema and migrations
6. Redis integration
7. health endpoint
8. initial GitHub Actions workflow

After that, move directly into the evaluator and flag CRUD.

<!-- codex resume 019d11ac-5d66-72c2-956f-57af66348c5f -->
