# Contributing to Feature Flag Management System

Thanks for contributing.

## How to contribute

You can contribute by:

- reporting bugs
- proposing features or improvements
- improving documentation
- submitting code changes with tests

## Before you start

Please read these documents first:

- `README.md`
- `docs/SRS-v1.md`
- `docs/PROJECT-BLUEPRINT.md`
- `docs/ROADMAP.md`
- `CODE_OF_CONDUCT.md`

## Development setup

Use the repository-local instructions as source of truth whenever they are added.

General baseline setup:

1. Fork and clone the repository.
2. Create a feature branch from `main`.
3. Install dependencies for all active apps/packages.
4. Start local services (for example PostgreSQL and Redis) if required.
5. Run tests and linters before opening a pull request.

## Branch and commit guidance

- Branch naming examples:
  - `feat/flag-evaluation-api`
  - `fix/audit-log-filter-bug`
  - `docs/readme-quickstart`
- Keep commits focused and atomic.
- Use clear commit messages that explain why the change is needed.

## Pull request checklist

When submitting a PR:

1. Link any relevant issue.
2. Describe the problem and the approach.
3. Include screenshots for UI changes when applicable.
4. Add or update tests for behavior changes.
5. Update docs when APIs, flows, or architecture assumptions change.
6. Ensure CI checks pass.

## Coding expectations

- Follow existing style and project conventions.
- Prefer simple, maintainable solutions over premature optimization.
- Validate inputs and preserve safe fallback behavior for evaluation paths.
- Keep behavior aligned with SRS requirements (especially deterministic evaluation and environment isolation).

## Testing expectations

Contributions should include tests appropriate to the change, such as:

- unit tests for rule logic and deterministic rollout behavior
- integration tests for API and persistence flows
- regression tests for bug fixes

If tests are not feasible for a change, explain why in the PR.

## Documentation expectations

If your change affects behavior, update the relevant docs:

- `README.md`
- `docs/PROJECT-BLUEPRINT.md`
- `docs/ROADMAP.md`
- future API or deployment docs when introduced

## Security issues

Do not open public issues for security vulnerabilities. Follow `SECURITY.md` for responsible disclosure.

## Questions and discussion

Use GitHub Issues for bugs and feature proposals. Keep discussions constructive and specific.
