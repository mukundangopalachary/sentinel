# Feature Flag Management System

Ship faster, release safer.

Feature Flag Management System is an open-source, self-hosted platform that lets you control feature releases at runtime without redeploying your apps.

Use it to launch gradually, test safely, and turn off risky behavior in seconds.

## Why teams use feature flags

- decouple deployment from release
- roll out to a small percentage before full launch
- target specific users or environments
- recover quickly by disabling a flag instead of redeploying
- keep release control centralized across services

## Core capabilities

- application and environment management (`dev`, `staging`, `prod`)
- feature flag CRUD with validation
- global toggle, percentage rollout, and user targeting rules
- deterministic runtime evaluation API
- audit history for flag changes
- dashboard for operational control
- REST integration and SDK support

## Quickstart

The project is currently in active build milestones.

To get started right now:

1. Read the requirements in `docs/SRS-v1.md`.
2. Review implementation details in `docs/PROJECT-BLUEPRINT.md`.
3. Check delivery progress in `docs/ROADMAP.md`.
4. Follow contribution setup in `CONTRIBUTING.md`.

## Product flow

1. Register an application and environment.
2. Create a feature flag.
3. Add rollout/targeting rules.
4. Evaluate the flag from your service at runtime.
5. Enable, disable, or adjust rollout instantly.

## Architecture at a glance

- backend service for management APIs and evaluation engine
- web dashboard for admins and developers
- PostgreSQL for persistent configuration and audit logs
- Redis for low-latency flag reads
- health checks, logs, and metrics for observability

## Documentation

- Requirements: `docs/SRS-v1.md`
- Blueprint: `docs/PROJECT-BLUEPRINT.md`
- Roadmap: `docs/ROADMAP.md`
- Contributing: `CONTRIBUTING.md`
- Code of Conduct: `CODE_OF_CONDUCT.md`
- Security: `SECURITY.md`

## Contributing

Contributions are welcome. Start with `CONTRIBUTING.md` for development and pull request guidelines.

## License

Licensed under `LICENSE`.
