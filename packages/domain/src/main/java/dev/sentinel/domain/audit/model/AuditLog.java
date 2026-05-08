package dev.sentinel.domain.audit.model;

import dev.sentinel.domain.shared.enums.AuditAction;
import dev.sentinel.domain.shared.exception.ValidationException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an immutable audit log entry recording a system action or change.
 * 
 * <p>Audit logs track all critical actions in the system (creates, updates, deletes, etc.)
 * for compliance, debugging, and audit trail purposes. Each entry captures the actor
 * (user), action type, affected entity, timestamps, and before/after values.
 * 
 * <p>Immutability: All fields are immutable as audit logs should never be modified
 * after creation.
 * 
 * @author Sentinel Team
 */
public final class AuditLog {

  private final UUID id;
  private final UUID actorUserId;
  private final String entityType;
  private final UUID entityId;
  private final AuditAction action;
  private final String oldValueJson;
  private final String newValueJson;
  private final String ipAddress;
  private final String userAgent;
  private final Instant createdAt;

  /**
   * Constructs a new AuditLog entry.
   * 
   * @param id the unique identifier for this audit log entry (never null)
   * @param actorUserId the ID of the user who performed the action (never null)
   * @param entityType the type of entity affected (e.g., "FeatureFlag", "Application"; never blank)
   * @param entityId the ID of the affected entity (never null)
   * @param action the type of action performed (CREATE, UPDATE, DELETE, etc.; never null)
   * @param oldValueJson optional JSON representation of the old value (can be null)
   * @param newValueJson optional JSON representation of the new value (can be null)
   * @param ipAddress optional IP address of the requester (can be null)
   * @param userAgent optional User-Agent header from the request (can be null)
   * @param createdAt the timestamp when the action was logged (never null)
   * @throws NullPointerException if any required parameter is null
   * @throws ValidationException if entityType is blank
   */
  public AuditLog(
      UUID id,
      UUID actorUserId,
      String entityType,
      UUID entityId,
      AuditAction action,
      String oldValueJson,
      String newValueJson,
      String ipAddress,
      String userAgent,
      Instant createdAt) {
    this.id = Objects.requireNonNull(id, "Audit log id cannot be null");
    this.actorUserId = Objects.requireNonNull(actorUserId, "Actor user id cannot be null");
    this.entityType = normalizeRequiredText(entityType, "Entity type cannot be blank");
    this.entityId = Objects.requireNonNull(entityId, "Entity id cannot be null");
    this.action = Objects.requireNonNull(action, "Audit action cannot be null");
    this.oldValueJson = normalizeOptionalText(oldValueJson);
    this.newValueJson = normalizeOptionalText(newValueJson);
    this.ipAddress = normalizeOptionalText(ipAddress);
    this.userAgent = normalizeOptionalText(userAgent);
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
  }

  /**
   * Gets the unique identifier of this audit log entry.
   * 
   * @return the audit log ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the user who performed the action.
   * 
   * @return the actor user ID (never null)
   */
  public UUID actorUserId() {
    return actorUserId;
  }

  /**
   * Gets the type of entity that was affected.
   * 
   * @return the entity type (e.g., "FeatureFlag"; never blank)
   */
  public String entityType() {
    return entityType;
  }

  /**
   * Gets the ID of the affected entity.
   * 
   * @return the entity ID (never null)
   */
  public UUID entityId() {
    return entityId;
  }

  /**
   * Gets the type of action that was performed.
   * 
   * @return the audit action (CREATE, UPDATE, DELETE, etc.)
   */
  public AuditAction action() {
    return action;
  }

  /**
   * Gets the JSON representation of the old value before the change.
   * 
   * @return the old value JSON, or null if not captured
   */
  public String oldValueJson() {
    return oldValueJson;
  }

  /**
   * Gets the JSON representation of the new value after the change.
   * 
   * @return the new value JSON, or null if not captured
   */
  public String newValueJson() {
    return newValueJson;
  }

  /**
   * Gets the IP address from which the request originated.
   * 
   * @return the IP address, or null if not captured
   */
  public String ipAddress() {
    return ipAddress;
  }

  /**
   * Gets the User-Agent header from the request.
   * 
   * @return the User-Agent string, or null if not captured
   */
  public String userAgent() {
    return userAgent;
  }

  /**
   * Gets the timestamp when this action was logged.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Checks if this audit log entry captures a value change.
   * 
   * <p>Returns true if at least one of oldValueJson or newValueJson is set.
   * 
   * @return true if a change was captured, false otherwise
   */
  public boolean capturesChange() {
    return oldValueJson != null || newValueJson != null;
  }

  /**
   * Checks if this audit log entry is for a specific entity.
   * 
   * @param entityType the entity type to match
   * @param entityId the entity ID to match
   * @return true if this entry is for the specified entity, false otherwise
   */
  public boolean isForEntity(String entityType, UUID entityId) {
    return this.entityType.equals(entityType) && this.entityId.equals(entityId);
  }

  /**
   * Validates and normalizes a required text field.
   * 
   * <p>Trims whitespace and ensures the text is not blank.
   * 
   * @param value the value to normalize (must not be null)
   * @param message the error message if validation fails
   * @return the normalized value
   * @throws NullPointerException if value is null
   * @throws ValidationException if value is blank after trimming
   */
  private static String normalizeRequiredText(String value, String message) {
    Objects.requireNonNull(value, message);

    String normalized = value.trim();
    if (normalized.isBlank()) {
      throw new ValidationException(message);
    }

    return normalized;
  }

  /**
   * Validates and normalizes an optional text field.
   * 
   * <p>Trims whitespace and returns null if the result is blank.
   * 
   * @param value the value to normalize (can be null)
   * @return the normalized value, or null if blank/null
   */
  private static String normalizeOptionalText(String value) {
    if (value == null) {
      return null;
    }

    String normalized = value.trim();
    return normalized.isBlank() ? null : normalized;
  }

  /**
   * Compares this audit log entry with another object for equality.
   * 
   * <p>Two entries are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are audit logs with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AuditLog)) {
      return false;
    }

    AuditLog other = (AuditLog) obj;
    return id.equals(other.id);
  }

  /**
   * Computes a hash code for this audit log entry based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
