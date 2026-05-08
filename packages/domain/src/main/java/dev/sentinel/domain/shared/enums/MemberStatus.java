package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of application member statuses.
 * 
 * MemberStatus represents the lifecycle state of a user's membership in an application.
 * Members transition through these states based on invitations, acceptance, and removal.
 * 
 * @author Mukundan Gopalachary
 */
public enum MemberStatus {
  /**
   * Pending status: User has been invited to join the application but has not yet accepted.
   */
  PENDING,
  /**
   * Active status: User has accepted the invitation and is an active member of the application.
   */
  ACTIVE,
  /**
   * Removed status: User has been removed from the application and no longer has access.
   */
  REMOVED
}
