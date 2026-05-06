package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.RoleType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Role {

  private final UUID id;
  private final RoleType name;
  private String description;
  private boolean systemRole;
  private final Instant createdAt;
  private Instant updatedAt;

  public Role(
      UUID id,
      RoleType name,
      String description,
      boolean systemRole,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Role id cannot be null");
    this.name = Objects.requireNonNull(name, "Role name cannot be null");
    this.description = normalizeDescription(description);
    this.systemRole = systemRole;
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public RoleType name() {
    return name;
  }

  public String description() {
    return description;
  }

  public boolean systemRole() {
    return systemRole;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant updatedAt() {
    return updatedAt;
  }

  public void changeDescription(String description, Instant updatedAt) {
    this.description = normalizeDescription(description);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void markAsSystemRole(Instant updatedAt) {
    this.systemRole = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public boolean isAssignable() {
    return true;
  }

  private static String normalizeDescription(String description) {
    if (description == null) {
      return null;
    }

    String normalized = description.trim();
    return normalized.isBlank() ? null : normalized;
  }

  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Role)) {
      return false;
    }

    Role other = (Role) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
