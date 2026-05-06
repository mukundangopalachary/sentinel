package dev.sentinel.domain.role.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class RolePermission {

  private final UUID id;
  private final UUID roleId;
  private final UUID permissionId;
  private final Instant createdAt;

  public RolePermission(UUID id, UUID roleId, UUID permissionId, Instant createdAt) {
    this.id = Objects.requireNonNull(id, "Role permission id cannot be null");
    this.roleId = Objects.requireNonNull(roleId, "Role id cannot be null");
    this.permissionId = Objects.requireNonNull(permissionId, "Permission id cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public UUID roleId() {
    return roleId;
  }

  public UUID permissionId() {
    return permissionId;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public boolean matches(UUID roleId, UUID permissionId) {
    return this.roleId.equals(roleId) && this.permissionId.equals(permissionId);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof RolePermission)) {
      return false;
    }

    RolePermission other = (RolePermission) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
