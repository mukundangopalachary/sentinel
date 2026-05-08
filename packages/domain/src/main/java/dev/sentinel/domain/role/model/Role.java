package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.RoleType;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a role that defines permissions for users within the system.
 * 
 * <p>Roles are used to manage access control and authorization. System roles
 * (like ADMIN, DEVELOPER, VIEWER) are predefined and cannot be deleted. Custom
 * roles can be created and managed by administrators.
 * 
 * <p>Immutability: The id, name, and createdAt fields are immutable. Other fields
 * can be modified through domain methods.
 * 
 * @author Mukundan Gopalachary
 */
public final class Role {

  private final UUID id;
  private final RoleType name;
  private String description;
  private boolean systemRole;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new Role.
   * 
   * @param id the unique identifier for this role (never null)
   * @param name the role type/name (ADMIN, DEVELOPER, VIEWER, OWNER; never null)
   * @param description optional description of the role's purpose (can be null)
   * @param systemRole whether this is a built-in system role
   * @param createdAt the timestamp when the role was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   */
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

  /**
   * Gets the unique identifier of this role.
   * 
   * @return the role ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the type/name of this role.
   * 
   * @return the role type (never null)
   */
  public RoleType name() {
    return name;
  }

  /**
   * Gets the optional description of this role.
   * 
   * @return the description, or null if not set
   */
  public String description() {
    return description;
  }

  /**
   * Checks if this is a built-in system role.
   * 
   * @return true if this is a system role, false otherwise
   */
  public boolean systemRole() {
    return systemRole;
  }

  /**
   * Gets the timestamp when this role was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this role.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Updates the description of this role.
   * 
   * @param description the new description (can be null or blank to clear)
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void changeDescription(String description, Instant updatedAt) {
    this.description = normalizeDescription(description);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Marks this role as a system role.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void markAsSystemRole(Instant updatedAt) {
    this.systemRole = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this role can be assigned to users.
   * 
   * @return true if assignable, false otherwise
   */
  public boolean isAssignable() {
    return true;
  }

  /**
   * Validates and normalizes a role description.
   * 
   * <p>Trims whitespace and returns null if the result is blank.
   * 
   * @param description the description to normalize (can be null)
   * @return the normalized description, or null if blank/null
   */
  private static String normalizeDescription(String description) {
    if (description == null) {
      return null;
    }

    String normalized = description.trim();
    return normalized.isBlank() ? null : normalized;
  }

  /**
   * Validates that updatedAt is not null.
   * 
   * @param updatedAt the timestamp to validate (must not be null)
   * @return the provided timestamp
   * @throws NullPointerException if updatedAt is null
   */
  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  /**
   * Compares this role with another object for equality.
   * 
   * <p>Two roles are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are roles with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this role based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
