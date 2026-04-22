# Entity Design

## Feature Flag Management System

This document defines the core domain entities for the Feature Flag Management System. It is intended to support:

- class diagrams
- ER diagrams
- database schema design
- service and API design
- implementation planning

The entities below are written in a class-style format so they can be translated into Java classes, database tables, and UML diagrams.

## 1. Design Principles

- keep authentication and authorization separate
- keep feature flag identity separate from rule logic
- keep application-scoped membership separate from global user identity
- keep auditability explicit
- keep runtime evaluation safe and deterministic

## 2. Value Types and Enums

These are not top-level entities, but they are part of the domain model.

## 2.1 RoleType

```text
OWNER
ADMIN
DEVELOPER
VIEWER
```

## 2.2 PermissionCode

```text
APPLICATION_READ
APPLICATION_CREATE
APPLICATION_UPDATE
APPLICATION_DELETE
ENVIRONMENT_READ
ENVIRONMENT_CREATE
ENVIRONMENT_UPDATE
ENVIRONMENT_DELETE
FLAG_READ
FLAG_CREATE
FLAG_UPDATE
FLAG_DELETE
FLAG_ENABLE
FLAG_DISABLE
RULE_READ
RULE_CREATE
RULE_UPDATE
RULE_DELETE
AUDIT_READ
MEMBER_INVITE
MEMBER_REMOVE
MEMBER_ROLE_ASSIGN
TOKEN_CREATE
TOKEN_REVOKE
```

## 2.3 EnvironmentType

```text
DEV
STAGING
PROD
CUSTOM
```

## 2.4 RuleType

```text
GLOBAL
PERCENTAGE_ROLLOUT
USER_TARGET
USER_GROUP_TARGET
ATTRIBUTE_MATCH
```

Only the first three should be implemented in v1. The others are placeholders for future extensibility.

## 2.5 AuditAction

```text
CREATE
UPDATE
DELETE
ENABLE
DISABLE
LOGIN
LOGOUT
INVITE
ROLE_ASSIGN
TOKEN_CREATE
TOKEN_REVOKE
```

## 2.6 MemberStatus

```text
PENDING
ACTIVE
REMOVED
```

## 2.7 TokenStatus

```text
ACTIVE
REVOKED
EXPIRED
```

## 3. Core Entities

## 3.1 User

Represents a platform user who can log in to the dashboard and perform management operations.

### Fields

- `id: UUID`
- `email: String`
- `passwordHash: String`
- `displayName: String`
- `isActive: Boolean`
- `lastLoginAt: Instant`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- one `User` can have many `ApplicationMember` entries
- one `User` can create many `Application` entries
- one `User` can create many `FeatureFlag` entries
- one `User` can generate many `AuditLog` entries
- one `User` can create many `ServiceToken` entries

### Methods

- `activate(): void`
- `deactivate(): void`
- `recordLogin(at: Instant): void`
- `changeDisplayName(displayName: String): void`
- `changePassword(passwordHash: String): void`
- `isEligibleForLogin(): Boolean`

## 3.2 Role

Represents a reusable application-scoped role such as `OWNER`, `ADMIN`, `DEVELOPER`, or `VIEWER`.

### Fields

- `id: UUID`
- `name: RoleType`
- `description: String`
- `isSystemRole: Boolean`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- one `Role` has many `RolePermission` mappings
- one `Role` can be assigned to many `ApplicationMember` entries

### Methods

- `rename(description: String): void`
- `markAsSystemRole(): void`
- `isAssignable(): Boolean`

## 3.3 Permission

Represents one atomic permission that can be granted to a role.

### Fields

- `id: UUID`
- `code: PermissionCode`
- `description: String`
- `createdAt: Instant`

### Relationships

- one `Permission` can belong to many `RolePermission` mappings

### Methods

- `sameCodeAs(other: Permission): Boolean`

## 3.4 RolePermission

Join entity connecting roles and permissions.

### Fields

- `id: UUID`
- `roleId: UUID`
- `permissionId: UUID`
- `createdAt: Instant`

### Relationships

- many `RolePermission` entries belong to one `Role`
- many `RolePermission` entries belong to one `Permission`

### Methods

- `matches(roleId: UUID, permissionId: UUID): Boolean`

## 3.5 Application

Represents a client product or service that uses feature flags.

### Fields

- `id: UUID`
- `key: String`
- `name: String`
- `description: String`
- `status: String`
- `createdBy: UUID`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- one `Application` has many `Environment` entries
- one `Application` has many `FeatureFlag` entries
- one `Application` has many `ApplicationMember` entries
- one `Application` has many `ServiceToken` entries

### Methods

- `rename(name: String): void`
- `changeDescription(description: String): void`
- `archive(): void`
- `restore(): void`
- `isActive(): Boolean`

## 3.6 Environment

Represents one deployable environment within an application, such as `dev`, `staging`, or `prod`.

### Fields

- `id: UUID`
- `applicationId: UUID`
- `key: String`
- `name: String`
- `type: EnvironmentType`
- `isProtected: Boolean`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- many `Environment` entries belong to one `Application`
- one `Environment` has many `FeatureFlag` entries
- one `Environment` has many `ServiceToken` entries

### Methods

- `rename(name: String): void`
- `markProtected(): void`
- `unmarkProtected(): void`
- `isProductionLike(): Boolean`

## 3.7 ApplicationMember

Represents the membership of a user within a specific application. This is the correct place to store app-scoped roles.

### Fields

- `id: UUID`
- `applicationId: UUID`
- `userId: UUID`
- `roleId: UUID`
- `status: MemberStatus`
- `invitedBy: UUID`
- `joinedAt: Instant`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- many `ApplicationMember` entries belong to one `Application`
- many `ApplicationMember` entries belong to one `User`
- many `ApplicationMember` entries belong to one `Role`

### Methods

- `assignRole(roleId: UUID): void`
- `activate(): void`
- `remove(): void`
- `isActive(): Boolean`
- `canBeModifiedBy(actorUserId: UUID): Boolean`

## 3.8 FeatureFlag

Represents the identity and metadata of a feature flag for a specific application and environment.

### Fields

- `id: UUID`
- `applicationId: UUID`
- `environmentId: UUID`
- `key: String`
- `name: String`
- `description: String`
- `enabled: Boolean`
- `archived: Boolean`
- `createdBy: UUID`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- many `FeatureFlag` entries belong to one `Application`
- many `FeatureFlag` entries belong to one `Environment`
- one `FeatureFlag` has many `FlagRule` entries
- one `FeatureFlag` has many `AuditLog` entries indirectly by entity reference

### Methods

- `enable(): void`
- `disable(): void`
- `archive(): void`
- `restore(): void`
- `rename(name: String): void`
- `changeDescription(description: String): void`
- `isEvaluable(): Boolean`

## 3.9 FlagRule

Represents one rule belonging to a feature flag. A flag can have multiple rules evaluated in a defined order.

### Fields

- `id: UUID`
- `featureFlagId: UUID`
- `ruleType: RuleType`
- `priority: Integer`
- `enabled: Boolean`
- `configJson: Json`
- `createdAt: Instant`
- `updatedAt: Instant`

### Relationships

- many `FlagRule` entries belong to one `FeatureFlag`

### Methods

- `changePriority(priority: Integer): void`
- `enable(): void`
- `disable(): void`
- `updateConfig(configJson: Json): void`
- `isApplicableTo(userId: String, attributes: Map<String, Object>): Boolean`
- `validate(): Boolean`

### Notes

For v1, `configJson` can contain:

- global rule config:
  - `{ "enabled": true }`
- percentage rollout config:
  - `{ "percentage": 25 }`
- user target config:
  - `{ "includedUserIds": ["u1", "u2"] }`

## 3.10 ServiceToken

Represents a credential used by external applications or SDKs to call the evaluation API.

### Fields

- `id: UUID`
- `applicationId: UUID`
- `environmentId: UUID`
- `name: String`
- `tokenHash: String`
- `status: TokenStatus`
- `expiresAt: Instant`
- `lastUsedAt: Instant`
- `createdBy: UUID`
- `createdAt: Instant`
- `revokedAt: Instant`

### Relationships

- many `ServiceToken` entries belong to one `Application`
- many `ServiceToken` entries belong to one `Environment`
- many `ServiceToken` entries can be created by one `User`

### Methods

- `markUsed(at: Instant): void`
- `revoke(at: Instant): void`
- `isActive(now: Instant): Boolean`
- `isExpired(now: Instant): Boolean`

## 3.11 AuditLog

Represents a traceable administrative action.

### Fields

- `id: UUID`
- `actorUserId: UUID`
- `entityType: String`
- `entityId: UUID`
- `action: AuditAction`
- `oldValueJson: Json`
- `newValueJson: Json`
- `ipAddress: String`
- `userAgent: String`
- `createdAt: Instant`

### Relationships

- many `AuditLog` entries can belong to one `User` as actor

### Methods

- `capturesChange(): Boolean`
- `isForEntity(entityType: String, entityId: UUID): Boolean`

## 4. Runtime and Supporting Models

These may not all map directly to Postgres tables, but they should exist in the domain or application layer.

## 4.1 EvaluationRequest

Represents the input to the runtime evaluation API.

### Fields

- `applicationKey: String`
- `environmentKey: String`
- `flagKey: String`
- `userId: String`
- `attributes: Map<String, Object>`
- `token: String`

### Methods

- `hasUser(): Boolean`
- `validateRequiredFields(): Boolean`

## 4.2 EvaluationResult

Represents the output of a flag evaluation.

### Fields

- `flagKey: String`
- `enabled: Boolean`
- `reason: String`
- `matchedRuleId: UUID`
- `evaluatedAt: Instant`

### Methods

- `isFallback(): Boolean`
- `isPositive(): Boolean`

## 4.3 FlagConfigSnapshot

Represents the cached view of a flag and its rules, typically stored in Redis.

### Fields

- `applicationKey: String`
- `environmentKey: String`
- `flagKey: String`
- `flagEnabled: Boolean`
- `archived: Boolean`
- `rules: List<FlagRule>`
- `version: Long`
- `loadedAt: Instant`

### Methods

- `isUsable(): Boolean`
- `findRulesByPriority(): List<FlagRule>`

## 4.4 AccessDecision

Represents the result of an authorization check.

### Fields

- `allowed: Boolean`
- `permissionCode: PermissionCode`
- `reason: String`

### Methods

- `deny(reason: String): AccessDecision`
- `allow(): AccessDecision`

## 5. Relationships Summary

- `User` 1 --- N `ApplicationMember`
- `Role` 1 --- N `ApplicationMember`
- `Role` 1 --- N `RolePermission`
- `Permission` 1 --- N `RolePermission`
- `Application` 1 --- N `Environment`
- `Application` 1 --- N `ApplicationMember`
- `Application` 1 --- N `FeatureFlag`
- `Application` 1 --- N `ServiceToken`
- `Environment` 1 --- N `FeatureFlag`
- `Environment` 1 --- N `ServiceToken`
- `FeatureFlag` 1 --- N `FlagRule`
- `User` 1 --- N `AuditLog`

## 6. Recommended Database Constraints

## 6.1 Unique Constraints

- `users.email` unique
- `roles.name` unique
- `permissions.code` unique
- `applications.key` unique
- `(application_id, key)` unique on `environments`
- `(application_id, environment_id, key)` unique on `feature_flags`
- `(application_id, user_id)` unique on `application_members`
- `(role_id, permission_id)` unique on `role_permissions`
- `service_tokens.token_hash` unique

## 6.2 Foreign Keys

- `environments.application_id -> applications.id`
- `application_members.application_id -> applications.id`
- `application_members.user_id -> users.id`
- `application_members.role_id -> roles.id`
- `feature_flags.application_id -> applications.id`
- `feature_flags.environment_id -> environments.id`
- `flag_rules.feature_flag_id -> feature_flags.id`
- `role_permissions.role_id -> roles.id`
- `role_permissions.permission_id -> permissions.id`
- `service_tokens.application_id -> applications.id`
- `service_tokens.environment_id -> environments.id`
- `audit_logs.actor_user_id -> users.id`

## 7. What Should Be Stored in Redis

Redis should not store the source of truth. It should store cached runtime objects.

## 7.1 Cached Objects

- `FlagConfigSnapshot`
- `ServiceToken` lookup cache
- optional session cache
- optional short-lived evaluation cache

## 7.2 Suggested Redis Keys

- `ff:{applicationKey}:{environmentKey}:{flagKey}`
- `token:{tokenHash}`
- `session:{sessionId}`
- `eval:{applicationKey}:{environmentKey}:{flagKey}:{userHash}`

## 8. Suggested UML Diagrams Based on This Document

- class diagram for all entities in section 3
- class diagram for runtime models in section 4
- ER diagram for the Postgres-backed entities
- sequence diagram for:
  - create application
  - invite member and assign role
  - create or update flag
  - evaluate flag with Redis cache miss

## 9. Recommended v1 Entity Set

If you want the smallest complete v1 set, use these first:

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

This set is enough to support:

- admin login
- app-scoped role assignment
- application/environment management
- feature flag management
- runtime evaluation
- auditability
