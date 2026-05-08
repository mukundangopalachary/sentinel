package dev.sentinel.domain.token.model;

import dev.sentinel.domain.shared.enums.TokenStatus;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.TokenHash;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a service token for API authentication and runtime flag evaluation.
 * 
 * <p>Service tokens are long-lived credentials used by applications to authenticate
 * with the feature flag management API. Tokens can be revoked, have expiration times,
 * and track usage through lastUsedAt timestamps.
 * 
 * <p>Immutability: The id, applicationId, environmentId, tokenHash, createdBy, and createdAt
 * fields are immutable. Other fields can be modified through domain methods.
 * 
 * @author Sentinel Team
 */
public final class ServiceToken {

  private final UUID id;
  private final UUID applicationId;
  private final UUID environmentId;
  private String name;
  private final TokenHash tokenHash;
  private TokenStatus status;
  private final Instant expiresAt;
  private Instant lastUsedAt;
  private final UUID createdBy;
  private final Instant createdAt;
  private Instant revokedAt;

  /**
   * Constructs a new ServiceToken.
   * 
   * @param id the unique identifier for this token (never null)
   * @param applicationId the ID of the application (never null)
   * @param environmentId the ID of the environment (never null)
   * @param name the human-readable name of the token (never blank)
   * @param tokenHash the hash of the actual token value (never null)
   * @param status the current token status (ACTIVE, REVOKED, EXPIRED; never null)
   * @param expiresAt the expiration timestamp (can be null for non-expiring tokens)
   * @param lastUsedAt the timestamp of last usage (can be null initially)
   * @param createdBy the user ID of the creator (never null)
   * @param createdAt the timestamp when the token was created (never null)
   * @param revokedAt the timestamp when the token was revoked (can be null)
   * @throws NullPointerException if any required parameter is null
   * @throws ValidationException if name is blank
   */
  public ServiceToken(
      UUID id,
      UUID applicationId,
      UUID environmentId,
      String name,
      TokenHash tokenHash,
      TokenStatus status,
      Instant expiresAt,
      Instant lastUsedAt,
      UUID createdBy,
      Instant createdAt,
      Instant revokedAt) {
    this.id = Objects.requireNonNull(id, "Service token id cannot be null");
    this.applicationId = Objects.requireNonNull(applicationId, "Application id cannot be null");
    this.environmentId = Objects.requireNonNull(environmentId, "Environment id cannot be null");
    this.name = normalizeName(name);
    this.tokenHash = Objects.requireNonNull(tokenHash, "Token hash cannot be null");
    this.status = Objects.requireNonNull(status, "Token status cannot be null");
    this.expiresAt = expiresAt;
    this.lastUsedAt = lastUsedAt;
    this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.revokedAt = revokedAt;
  }

  /**
   * Gets the unique identifier of this token.
   * 
   * @return the token ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the application this token is for.
   * 
   * @return the application ID (never null)
   */
  public UUID applicationId() {
    return applicationId;
  }

  /**
   * Gets the ID of the environment this token is for.
   * 
   * @return the environment ID (never null)
   */
  public UUID environmentId() {
    return environmentId;
  }

  /**
   * Gets the human-readable name of this token.
   * 
   * @return the token name (never blank)
   */
  public String name() {
    return name;
  }

  /**
   * Gets the hash of the token value.
   * 
   * @return the token hash (never null)
   */
  public TokenHash tokenHash() {
    return tokenHash;
  }

  /**
   * Gets the current status of this token.
   * 
   * @return the token status (ACTIVE, REVOKED, or EXPIRED)
   */
  public TokenStatus status() {
    return status;
  }

  /**
   * Gets the expiration timestamp for this token.
   * 
   * @return the expiration time, or null if token does not expire
   */
  public Instant expiresAt() {
    return expiresAt;
  }

  /**
   * Gets the timestamp of the last usage of this token.
   * 
   * @return the last used timestamp, or null if never used
   */
  public Instant lastUsedAt() {
    return lastUsedAt;
  }

  /**
   * Gets the ID of the user who created this token.
   * 
   * @return the creator's user ID (never null)
   */
  public UUID createdBy() {
    return createdBy;
  }

  /**
   * Gets the timestamp when this token was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp when this token was revoked.
   * 
   * @return the revocation timestamp, or null if not revoked
   */
  public Instant revokedAt() {
    return revokedAt;
  }

  /**
   * Records the usage of this token.
   * 
   * @param lastUsedAt the timestamp of the usage (never null)
   * @throws NullPointerException if lastUsedAt is null
   */
  public void markUsed(Instant lastUsedAt) {
    this.lastUsedAt = Objects.requireNonNull(lastUsedAt, "Last used at cannot be null");
  }

  /**
   * Revokes this token, making it inactive for future requests.
   * 
   * @param revokedAt the timestamp of the revocation (never null)
   * @throws NullPointerException if revokedAt is null
   */
  public void revoke(Instant revokedAt) {
    this.status = TokenStatus.REVOKED;
    this.revokedAt = Objects.requireNonNull(revokedAt, "Revoked at cannot be null");
  }

  /**
   * Checks if this token is currently active and usable.
   * 
   * @param now the current timestamp for comparison (never null)
   * @return true if token is ACTIVE and not expired, false otherwise
   * @throws NullPointerException if now is null
   */
  public boolean isActive(Instant now) {
    Objects.requireNonNull(now, "Current time cannot be null");
    return status == TokenStatus.ACTIVE && !isExpired(now);
  }

  /**
   * Checks if this token has expired.
   * 
   * @param now the current timestamp for comparison (never null)
   * @return true if token has an expiration time and now is after it, false otherwise
   * @throws NullPointerException if now is null
   */
  public boolean isExpired(Instant now) {
    Objects.requireNonNull(now, "Current time cannot be null");
    return expiresAt != null && now.isAfter(expiresAt);
  }

  /**
   * Validates and normalizes a service token name.
   * 
   * <p>Trims whitespace and ensures the name is not blank.
   * 
   * @param name the name to normalize (must not be null)
   * @return the normalized name
   * @throws NullPointerException if name is null
   * @throws ValidationException if name is blank after trimming
   */
  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Service token name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Service token name cannot be blank");
    }

    return normalized;
  }

  /**
   * Compares this token with another object for equality.
   * 
   * <p>Two tokens are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are tokens with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ServiceToken)) {
      return false;
    }

    ServiceToken other = (ServiceToken) obj;
    return id.equals(other.id);
  }

  /**
   * Computes a hash code for this token based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
