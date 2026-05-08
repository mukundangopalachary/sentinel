package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of audit log action types.
 * 
 * AuditAction represents the different types of actions that can be recorded in the audit log.
 * Each audit entry captures who performed what action, when, and on which resource,
 * enabling compliance, debugging, and security monitoring.
 * 
 * @author Sentinel Team
 */
public enum AuditAction {
  /**
   * Create action: A new resource (application, environment, flag, role, token) was created.
   */
  CREATE,
  /**
   * Update action: An existing resource was modified.
   */
  UPDATE,
  /**
   * Delete action: A resource was deleted.
   */
  DELETE,
  /**
   * Enable action: A feature flag was enabled in an environment.
   */
  ENABLE,
  /**
   * Disable action: A feature flag was disabled in an environment.
   */
  DISABLE,
  /**
   * Login action: A user successfully authenticated and logged in.
   */
  LOGIN,
  /**
   * Logout action: A user logged out.
   */
  LOGOUT,
  /**
   * Invite action: A user was invited to join an application.
   */
  INVITE,
  /**
   * Role assign action: A role was assigned or changed for an application member.
   */
  ROLE_ASSIGN,
  /**
   * Token create action: A new API token was created.
   */
  TOKEN_CREATE,
  /**
   * Token revoke action: An API token was revoked.
   */
  TOKEN_REVOKE
}
