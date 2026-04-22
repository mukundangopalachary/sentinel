# UML Plan

## Feature Flag Management System

This document defines which UML diagrams should be created for the Feature Flag Management System, in what order they should be drawn, and what each diagram must include.

The goal is not to produce every UML diagram type. The goal is to create the few diagrams that help with:

- domain modeling
- system behavior design
- architecture communication
- implementation planning

## 1. Recommended Drawing Order

Draw the diagrams in this order:

1. class diagram
2. ER-style data model diagram
3. sequence diagram for feature flag evaluation
4. sequence diagram for flag update and cache refresh
5. activity diagram for rule evaluation logic
6. use case diagram
7. component diagram
8. deployment diagram

This order is important because:

- data and relationships should be clear before runtime flows
- runtime flows should be clear before deployment views
- actor/use case diagrams are useful, but they should not drive your schema design

## 2. Diagram Set

## 2.1 Class Diagram

### Priority

Highest priority. Draw this first.

### Purpose

Use this to model the core domain and their relationships.

### Include These Classes

- `User`
- `Role`
- `Permission`
- `RolePermission`
- `Application`
- `Environment`
- `ApplicationMember`
- `FeatureFlag`
- `FlagRule`
- `ServiceToken`
- `AuditLog`
- `EvaluationRequest`
- `EvaluationResult`
- `FlagConfigSnapshot`
- `AccessDecision`

### Include These Enums

- `RoleType`
- `PermissionCode`
- `EnvironmentType`
- `RuleType`
- `AuditAction`
- `MemberStatus`
- `TokenStatus`

### Show for Each Class

- class name
- fields
- main methods
- associations
- multiplicities

### Required Relationships

- `User 1 --- * ApplicationMember`
- `Role 1 --- * ApplicationMember`
- `Role 1 --- * RolePermission`
- `Permission 1 --- * RolePermission`
- `Application 1 --- * Environment`
- `Application 1 --- * ApplicationMember`
- `Application 1 --- * FeatureFlag`
- `Application 1 --- * ServiceToken`
- `Environment 1 --- * FeatureFlag`
- `Environment 1 --- * ServiceToken`
- `FeatureFlag 1 --- * FlagRule`
- `User 1 --- * AuditLog`

### Notes

- treat `RolePermission` as an explicit class, not a hidden join
- keep `FeatureFlag` and `FlagRule` separate
- show `ApplicationMember` clearly because it is critical to app-scoped authorization

## 2.2 ER-Style Data Model Diagram

### Priority

High. Draw right after the class diagram.

### Purpose

Use this to map the Postgres storage model.

### Include These Tables

- `users`
- `roles`
- `permissions`
- `role_permissions`
- `applications`
- `environments`
- `application_members`
- `feature_flags`
- `flag_rules`
- `service_tokens`
- `audit_logs`

### For Each Table Show

- primary key
- foreign keys
- unique constraints
- important indexed fields

### Required Constraints to Annotate

- `users.email` unique
- `roles.name` unique
- `permissions.code` unique
- `applications.key` unique
- `(application_id, key)` unique on `environments`
- `(application_id, environment_id, key)` unique on `feature_flags`
- `(application_id, user_id)` unique on `application_members`
- `(role_id, permission_id)` unique on `role_permissions`
- `service_tokens.token_hash` unique

### Notes

- this is not the same as the class diagram
- this diagram should stay relational and database-focused

## 2.3 Sequence Diagram: Feature Flag Evaluation

### Priority

Very high. Draw this after the data model.

### Purpose

Use this to define the runtime read path.

### Participants

- `Client Application / SDK`
- `Evaluation API`
- `Token Validator`
- `Redis Cache`
- `Postgres`
- `Rule Evaluator`

### Flow to Show

1. client sends evaluation request
2. API validates token
3. API checks Redis for flag config snapshot
4. if cache miss, API loads flag and rules from Postgres
5. API writes cache snapshot to Redis
6. evaluator applies rule ordering
7. API returns evaluation result
8. metrics and logs are recorded

### Important Branches

- invalid token
- flag not found
- cache hit
- cache miss
- rule match
- fallback disabled

## 2.4 Sequence Diagram: Flag Update and Cache Refresh

### Priority

Very high. Draw this after evaluation flow.

### Purpose

Use this to define the write path and consistency behavior.

### Participants

- `Admin User`
- `Dashboard`
- `Management API`
- `Authorization Service`
- `Postgres`
- `Audit Service`
- `Redis Cache`

### Flow to Show

1. admin submits flag update
2. API checks membership and permission
3. API validates rule config
4. API starts transaction
5. API updates flag and/or rules in Postgres
6. API writes audit log
7. API commits transaction
8. API invalidates or refreshes Redis cache
9. API returns success

### Important Branches

- unauthorized user
- invalid rule config
- DB write failure
- cache refresh failure after commit

### Notes

- make it clear that Postgres is the source of truth
- cache refresh should not replace transactional correctness

## 2.5 Activity Diagram: Rule Evaluation Logic

### Priority

High. Draw this once the sequence diagrams are done.

### Purpose

Use this to define the exact decision path in the evaluator.

### Start Condition

An `EvaluationRequest` arrives with:

- application key
- environment key
- flag key
- user ID
- optional attributes

### Flow to Show

1. validate request
2. find flag config
3. flag exists?
4. flag archived?
5. flag enabled?
6. sort enabled rules by priority
7. evaluate explicit user targeting
8. evaluate percentage rollout
9. return `enabled = true` if a rule matches
10. otherwise return `enabled = false`

### Required Decisions

- missing flag -> false
- disabled flag -> false
- archived flag -> false
- targeted user match -> true
- rollout match -> true
- no match -> false

### Notes

- this diagram is one of the most important for implementation
- keep it deterministic and simple

## 2.6 Use Case Diagram

### Priority

Medium. Useful for overview, not for low-level design.

### Purpose

Use this to show system actors and goals.

### Actors

- `Admin`
- `Developer`
- `Viewer`
- `Client Application / SDK`

### Use Cases

- login
- logout
- create application
- manage environments
- invite members
- assign member role
- create feature flag
- update feature flag
- enable or disable feature flag
- configure rules
- view flags
- view audit history
- evaluate feature flag
- create or revoke service token

### Notes

- `Developer` and `Admin` may overlap in permissions depending on app membership
- keep this diagram high-level

## 2.7 Component Diagram

### Priority

Medium. Draw after the core behavior diagrams.

### Purpose

Use this to communicate system/module boundaries.

### Include These Components

- `Web Dashboard`
- `Auth Module`
- `Authorization Module`
- `Application Management Module`
- `Environment Management Module`
- `Feature Flag Management Module`
- `Rule Management Module`
- `Evaluation Engine`
- `Audit Module`
- `Token Management Module`
- `Redis Cache Layer`
- `Postgres Persistence Layer`
- `Java SDK`

### Show These Dependencies

- dashboard -> management API modules
- evaluation engine -> cache layer
- evaluation engine -> persistence layer
- token management -> persistence layer
- audit module -> persistence layer
- authorization module -> roles/permissions/membership data
- SDK -> evaluation API

### Notes

- this is a good bridge between domain design and code structure

## 2.8 Deployment Diagram

### Priority

Medium. Draw when architecture is stable enough.

### Purpose

Use this to show where the system runs and how components connect.

### Include These Nodes

- `User Browser`
- `Client Application`
- `Web Dashboard`
- `API Service`
- `PostgreSQL`
- `Redis`
- optional `Coolify Host`
- optional `Docker Host`

### Show These Connections

- browser -> dashboard
- dashboard -> API service
- client application -> API service
- API service -> PostgreSQL
- API service -> Redis

### Optional Deployment Variants

- local development with Docker Compose
- self-hosted VPS deployment
- future Kubernetes deployment

## 3. Diagram-by-Diagram Checklist

Use this checklist while drawing.

## 3.1 Class Diagram Checklist

- all core entities included
- fields shown
- major methods shown
- multiplicities shown
- join entities shown explicitly
- enums shown

## 3.2 ER Diagram Checklist

- all tables included
- PKs shown
- FKs shown
- unique constraints shown
- no runtime-only objects mixed in

## 3.3 Evaluation Sequence Checklist

- Redis hit and miss path shown
- Postgres read path shown
- rule evaluator shown
- fallback false path shown
- token validation shown

## 3.4 Flag Update Sequence Checklist

- authorization shown
- transaction boundaries considered
- audit log shown
- cache invalidation shown

## 3.5 Activity Diagram Checklist

- all key decisions shown
- false fallback clearly shown
- rule order is explicit

## 3.6 Deployment Diagram Checklist

- API, Redis, Postgres shown
- dashboard shown
- client app shown
- communication paths shown

## 4. Diagrams You Should Not Prioritize Now

Avoid spending time on these for now:

- object diagrams
- package diagrams
- communication diagrams
- state machine diagrams for every entity

These may become useful later, but they are not the best use of time right now.

## 5. Best Minimal Set

If you want the smallest UML set that still covers the project well, draw only these:

1. class diagram
2. ER-style data model diagram
3. sequence diagram for feature flag evaluation
4. sequence diagram for flag update and cache refresh
5. activity diagram for rule evaluation
6. deployment diagram

That set is enough to support schema design, service design, evaluator design, and deployment design.

## 6. Recommended Drawing Style

- keep one diagram focused on one concern
- avoid mixing DB tables and runtime objects in the same diagram unless necessary
- use explicit cardinalities
- use consistent naming across all diagrams
- use the same entity names as defined in `docs/design/ENTITY-DESIGN.md`

## 7. Source of Truth

This UML plan should be used together with:

- `docs/design/ENTITY-DESIGN.md`
- `docs/PROJECT-BLUEPRINT.md`
- `docs/SRS-v1.md`

If a UML diagram conflicts with those documents, update the design docs first and then redraw the diagram.
