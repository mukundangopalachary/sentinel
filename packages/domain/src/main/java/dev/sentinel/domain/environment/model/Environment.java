package dev.sentinel.domain.environment.model;

import dev.sentinel.domain.shared.enums.EnvironmentType;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Environment {

  private final UUID id;
  private final UUID applicationId;
  private final EnvironmentKey key;
  private String name;
  private final EnvironmentType type;
  private boolean protectedEnvironment;
  private final Instant createdAt;
  private Instant updatedAt;

  public Environment(
      UUID id,
      UUID applicationId,
      EnvironmentKey key,
      String name,
      EnvironmentType type,
      boolean protectedEnvironment,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Environment id cannot be null");
    this.applicationId = Objects.requireNonNull(applicationId, "Application id cannot be null");
    this.key = Objects.requireNonNull(key, "Environment key cannot be null");
    this.name = normalizeName(name);
    this.type = Objects.requireNonNull(type, "Environment type cannot be null");
    this.protectedEnvironment = protectedEnvironment;
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public UUID applicationId() {
    return applicationId;
  }

  public EnvironmentKey key() {
    return key;
  }

  public String name() {
    return name;
  }

  public EnvironmentType type() {
    return type;
  }

  public boolean protectedEnvironment() {
    return protectedEnvironment;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant updatedAt() {
    return updatedAt;
  }

  public void rename(String name, Instant updatedAt) {
    this.name = normalizeName(name);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void markProtected(Instant updatedAt) {
    this.protectedEnvironment = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void unmarkProtected(Instant updatedAt) {
    this.protectedEnvironment = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public boolean isProtected() {
    return protectedEnvironment;
  }

  public boolean isProductionLike() {
    return type == EnvironmentType.PROD;
  }

  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Environment name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Environment name cannot be blank");
    }

    return normalized;
  }

  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Environment)) {
      return false;
    }

    Environment other = (Environment) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
