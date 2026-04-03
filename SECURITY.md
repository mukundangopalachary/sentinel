# Security Policy

## Supported Versions

This project is under active development. Until stable releases are published, security fixes are applied to the latest state of the `main` branch.

Once versioned releases are available, this table will be updated with maintained versions and support windows.

## Reporting a Vulnerability

Please do **not** report security vulnerabilities in public issues.

Instead, report privately to:

- **security@maintainers.example**

Include:

- a clear description of the issue
- impact and potential exploitation scenario
- affected components and versions/commit refs
- reproduction steps or proof-of-concept (if available)

You can expect:

- acknowledgement within 3 business days
- triage and severity assessment
- follow-up on mitigation and disclosure timing

## Disclosure Process

Our coordinated disclosure process is:

1. Receive and acknowledge the report.
2. Validate and assess severity.
3. Prepare and test a fix.
4. Coordinate disclosure details with the reporter.
5. Publish a security advisory with remediation guidance.

## Security Principles for this project

This project prioritizes:

- secure authentication and authorization (RBAC)
- safe default behavior (flag missing/error -> disabled fallback)
- input validation on management and evaluation APIs
- auditable administrative changes
- secure secret/token handling (hashed at rest)
- HTTPS usage in deployed environments

## Scope notes

Potentially sensitive areas include:

- authentication/session handling
- role and permission enforcement
- evaluation API and token validation
- audit log integrity
- cache consistency and stale configuration behavior

Please avoid sharing real credentials, production tokens, or private infrastructure details in reports.
