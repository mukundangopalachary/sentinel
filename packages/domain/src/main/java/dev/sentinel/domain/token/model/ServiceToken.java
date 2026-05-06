package dev.sentinel.domain.token.model;

import dev.sentinel.domain.shared.enums.TokenStatus;
import dev.sentinel.domain.shared.valueobject.TokenHash;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

  public UUID id() {
    return id;
  }

  public UUID applicationId() {
    return applicationId;
  }

  public UUID environmentId() {
    return environmentId;
  }

  public String name() {
    return name;
  }

  public TokenHash tokenHash() {
    return tokenHash;
  }

  public TokenStatus status() {
    return status;
  }

  public Instant expiresAt() {
    return expiresAt;
  }

  public Instant lastUsedAt() {
    return lastUsedAt;
  }

  public UUID createdBy() {
    return createdBy;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant revokedAt() {
    return revokedAt;
  }

  public void markUsed(Instant lastUsedAt) {
    this.lastUsedAt = Objects.requireNonNull(lastUsedAt, "Last used at cannot be null");
  }

  public void revoke(Instant revokedAt) {
    this.status = TokenStatus.REVOKED;
    this.revokedAt = Objects.requireNonNull(revokedAt, "Revoked at cannot be null");
  }

  public boolean isActive(Instant now) {
    Objects.requireNonNull(now, "Current time cannot be null");
    return status == TokenStatus.ACTIVE && !isExpired(now);
  }

  public boolean isExpired(Instant now) {
    Objects.requireNonNull(now, "Current time cannot be null");
    return expiresAt != null && now.isAfter(expiresAt);
  }

  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Service token name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Service token name cannot be blank");
    }

    return normalized;
  }

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

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
