package dev.sentinel.domain.shared.enums;


/**
 * Enumeration of predefined role types in the feature flag system.
 * 
 * RoleType represents the hierarchical roles available for application members.
 * Each role type carries a fixed set of permissions; custom roles are managed via the Role entity.
 * Roles control what operations members can perform on applications and their resources.
 * 
 * @author Sentinel Team
 */
public enum RoleType{
  /**
   * Owner role: Full administrative control; can manage members, delete application, invoke billing.
   */
  OWNER,
  /**
   * Admin role: Can manage all application resources (flags, environments, members, tokens),
   * but cannot delete application or manage billing.
   */
  ADMIN,
  /**
   * Developer role: Can create, update, and delete flags and rules; can create API tokens.
   */
  DEVELOPER,
  /**
   * Viewer role: Read-only access; can view flags, rules, and audit logs but cannot make changes.
   */
  VIEWER;
}
