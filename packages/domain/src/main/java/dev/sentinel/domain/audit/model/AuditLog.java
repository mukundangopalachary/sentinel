package dev.sentinel.domain.audit.model;

import dev.sentinel.domain.shared.enums.AuditAction;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

  public UUID id() {
    return id;
  }

  public UUID actorUserId() {
    return actorUserId;
  }

  public String entityType() {
    return entityType;
  }

  public UUID entityId() {
    return entityId;
  }

  public AuditAction action() {
    return action;
  }

  public String oldValueJson() {
    return oldValueJson;
  }

  public String newValueJson() {
    return newValueJson;
  }

  public String ipAddress() {
    return ipAddress;
  }

  public String userAgent() {
    return userAgent;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public boolean capturesChange() {
    return oldValueJson != null || newValueJson != null;
  }

  public boolean isForEntity(String entityType, UUID entityId) {
    return this.entityType.equals(entityType) && this.entityId.equals(entityId);
  }

  private static String normalizeRequiredText(String value, String message) {
    Objects.requireNonNull(value, message);

    String normalized = value.trim();
    if (normalized.isBlank()) {
      throw new IllegalArgumentException(message);
    }

    return normalized;
  }

  private static String normalizeOptionalText(String value) {
    if (value == null) {
      return null;
    }

    String normalized = value.trim();
    return normalized.isBlank() ? null : normalized;
  }

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

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
