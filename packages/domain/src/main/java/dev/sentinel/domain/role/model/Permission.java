package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.PermissionCode;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Permission {

  private final UUID id;
  private final PermissionCode code;
  private String description;
  private final Instant createdAt;

  public Permission(UUID id, PermissionCode code, String description, Instant createdAt) {
    this.id = Objects.requireNonNull(id, "Permission id cannot be null");
    this.code = Objects.requireNonNull(code, "Permission code cannot be null");
    this.description = normalizeDescription(description);
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public PermissionCode code() {
    return code;
  }

  public String description() {
    return description;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public boolean sameCodeAs(Permission other) {
    Objects.requireNonNull(other, "Permission cannot be null");
    return code == other.code;
  }

  private static String normalizeDescription(String description) {
    if (description == null) {
      return null;
    }

    String normalized = description.trim();
    return normalized.isBlank() ? null : normalized;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Permission)) {
      return false;
    }

    Permission other = (Permission) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
