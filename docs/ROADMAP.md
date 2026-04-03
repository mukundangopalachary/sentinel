# Roadmap

This roadmap outlines planned milestones for delivering the platform.

## Guiding principles

- deliver a reliable evaluation engine first
- keep management and evaluation concerns clearly separated
- favor safe defaults and operational control
- ship in milestone increments with testable outcomes

## Milestone 0 - Foundation

Goal: establish core repository and local platform skeleton.

- choose and lock stack direction (Java-first or TypeScript-first)
- initialize app/package workspace layout
- provision local Postgres and Redis via Docker Compose
- set up baseline CI checks (lint, test, build)

Exit criteria:

- local environment boots successfully
- CI runs on pull requests and main

## Milestone 1 - Core Data and CRUD

Goal: enable management of core entities.

- define schema for users, roles, applications, environments, flags, rules, audits
- add migrations and seed strategy
- implement application/environment CRUD APIs
- implement feature flag CRUD APIs with validation

Exit criteria:

- authenticated users can manage applications, environments, and flags
- uniqueness and validation constraints are enforced

## Milestone 2 - Evaluation Engine

Goal: deliver deterministic runtime flag evaluation.

- implement evaluation endpoint
- implement rule order and fallback behavior
- support global toggle, percentage rollout, user targeting, environment rules
- guarantee deterministic rollout for same user + flag inputs

Exit criteria:

- runtime evaluation is deterministic and returns safe defaults on missing flags/errors

## Milestone 3 - Caching and Auditability

Goal: improve latency and traceability.

- add Redis-backed flag configuration caching
- implement cache refresh/invalidation on flag updates
- implement audit log creation for create/update/enable/disable/delete operations
- add audit filtering by app, flag, user, and date

Exit criteria:

- updated flag configs are reflected quickly
- all privileged changes are auditable

## Milestone 4 - Security and Dashboard

Goal: provide a secure management experience.

- implement admin login/logout and session management
- enforce RBAC across management operations
- build dashboard for applications, flags, and rule editing
- add search/filter for faster navigation

Exit criteria:

- authorized roles can safely manage platform configuration via web UI

## Milestone 5 - SDK and Operability

Goal: make integration and operations production-ready.

- publish Java SDK with simple evaluation API
- define SDK configuration and safe fallback behavior
- expose health endpoint and operational metrics
- improve structured logging and error observability
- document backup and recovery steps

Exit criteria:

- external apps can integrate via SDK/API
- operators can monitor core service health and evaluation behavior

## Milestone 6 - Open-source release readiness

Goal: make the project contributor-friendly.

- finalize README and project docs
- maintain contribution, conduct, and security policies
- add issue and PR templates
- provide sample integration apps

Exit criteria:

- new contributors can clone, run, understand, and contribute with minimal friction

## Future milestones (post-v1)

- advanced targeting (time, region, user attributes)
- experimentation variants and analytics
- additional language SDKs
- external identity provider integrations
- deeper CI/CD and monitoring ecosystem integrations
- multi-region deployment patterns
