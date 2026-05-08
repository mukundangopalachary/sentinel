package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of possible service token statuses.
 * 
 * TokenStatus represents the lifecycle state of an API token used for programmatic access
 * to the feature flag system. Tokens transition through these states based on user actions
 * and time-based events.
 * 
 * @author Sentinel Team
 */
public enum TokenStatus {
  /**
   * Active status: Token is valid and can be used for API authentication and requests.
   */
  ACTIVE,
  /**
   * Revoked status: Token has been explicitly revoked by a user and is no longer valid.
   */
  REVOKED,
  /**
   * Expired status: Token has exceeded its expiration date and is no longer valid.
   */
  EXPIRED
}
