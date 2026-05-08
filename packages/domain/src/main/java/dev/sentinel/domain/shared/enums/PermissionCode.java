package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of granular permission codes in the feature flag system.
 * 
 * PermissionCode represents specific, granular permissions that can be assigned to roles
 * and controlled at a fine-grained level. Permissions are organized by resource type
 * (APPLICATION, ENVIRONMENT, FLAG, RULE, AUDIT, MEMBER, TOKEN) and operation (READ, CREATE, UPDATE, DELETE).
 * 
 * @author Sentinel Team
 */
public enum PermissionCode {
  /**
   * Permission to read application details and list applications.
   */
  APPLICATION_READ,
  /**
   * Permission to create new applications.
   */
  APPLICATION_CREATE,
  /**
   * Permission to update application details.
   */
  APPLICATION_UPDATE,
  /**
   * Permission to delete applications.
   */
  APPLICATION_DELETE,
  /**
   * Permission to read environment details and list environments.
   */
  ENVIRONMENT_READ,
  /**
   * Permission to create new environments.
   */
  ENVIRONMENT_CREATE,
  /**
   * Permission to update environment configuration.
   */
  ENVIRONMENT_UPDATE,
  /**
   * Permission to delete environments.
   */
  ENVIRONMENT_DELETE,
  /**
   * Permission to read feature flag details and list flags.
   */
  FLAG_READ,
  /**
   * Permission to create new feature flags.
   */
  FLAG_CREATE,
  /**
   * Permission to update feature flag configuration.
   */
  FLAG_UPDATE,
  /**
   * Permission to delete feature flags.
   */
  FLAG_DELETE,
  /**
   * Permission to enable feature flags.
   */
  FLAG_ENABLE,
  /**
   * Permission to disable feature flags.
   */
  FLAG_DISABLE,
  /**
   * Permission to read flag rules and list rules.
   */
  RULE_READ,
  /**
   * Permission to create new flag rules.
   */
  RULE_CREATE,
  /**
   * Permission to update flag rules.
   */
  RULE_UPDATE,
  /**
   * Permission to delete flag rules.
   */
  RULE_DELETE,
  /**
   * Permission to read and view audit logs.
   */
  AUDIT_READ,
  /**
   * Permission to invite new members to an application.
   */
  MEMBER_INVITE,
  /**
   * Permission to remove members from an application.
   */
  MEMBER_REMOVE,
  /**
   * Permission to assign or change roles for application members.
   */
  MEMBER_ROLE_ASSIGN,
  /**
   * Permission to create new API tokens for programmatic access.
   */
  TOKEN_CREATE,
  /**
   * Permission to revoke existing API tokens.
   */
  TOKEN_REVOKE;
}
