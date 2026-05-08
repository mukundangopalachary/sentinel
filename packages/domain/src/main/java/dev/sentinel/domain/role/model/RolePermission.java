package dev.sentinel.domain.role.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an association between a role and a permission.
 * 
 * <p>This junction entity defines which permissions a role has. A role can have
 * multiple permissions, and permissions can be assigned to multiple roles.
 * 
 * <p>Immutability: All fields are immutable.
 * 
 * @author Mukundan Gopalachary
 */
public final class RolePermission {

  private final UUID id;
  private final UUID roleId;
  private final UUID permissionId;
  private final Instant createdAt;

  /**
   * Constructs a new RolePermission association.
   * 
   * @param id the unique identifier for this association (never null)
   * @param roleId the ID of the role (never null)
   * @param permissionId the ID of the permission (never null)
   * @param createdAt the timestamp when the association was created (never null)
   * @throws NullPointerException if any parameter is null
   */
  public RolePermission(UUID id, UUID roleId, UUID permissionId, Instant createdAt) {
    this.id = Objects.requireNonNull(id, "Role permission id cannot be null");
    this.roleId = Objects.requireNonNull(roleId, "Role id cannot be null");
    this.permissionId = Objects.requireNonNull(permissionId, "Permission id cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
  }

  /**
   * Gets the unique identifier of this association.
   * 
   * @return the association ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the role in this association.
   * 
   * @return the role ID (never null)
   */
  public UUID roleId() {
    return roleId;
  }

  /**
   * Gets the ID of the permission in this association.
   * 
   * @return the permission ID (never null)
   */
  public UUID permissionId() {
    return permissionId;
  }

  /**
   * Gets the timestamp when this association was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Checks if this association matches the given role and permission IDs.
   * 
   * @param roleId the role ID to match
   * @param permissionId the permission ID to match
   * @return true if both IDs match this association, false otherwise
   */
  public boolean matches(UUID roleId, UUID permissionId) {
    return this.roleId.equals(roleId) && this.permissionId.equals(permissionId);
  }

  /**
   * Compares this association with another object for equality.
   * 
   * <p>Two associations are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are associations with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this association based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
