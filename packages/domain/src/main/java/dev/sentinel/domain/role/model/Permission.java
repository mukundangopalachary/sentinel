package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.PermissionCode;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a permission that can be assigned to roles.
 * 
 * <p>Permissions define granular actions or capabilities (e.g., FLAG_CREATE, AUDIT_READ).
 * Multiple permissions can be associated with a role via RolePermission associations.
 * 
 * <p>Immutability: The id, code, and createdAt fields are immutable. The description
 * can be modified.
 * 
 * @author Mukundan Gopalachary
 */
public final class Permission {

  private final UUID id;
  private final PermissionCode code;
  private String description;
  private final Instant createdAt;

  /**
   * Constructs a new Permission.
   * 
   * @param id the unique identifier for this permission (never null)
   * @param code the permission code (e.g., FLAG_CREATE; never null)
   * @param description optional description of the permission (can be null)
   * @param createdAt the timestamp when the permission was created (never null)
   * @throws NullPointerException if any non-optional parameter is null
   */
  public Permission(UUID id, PermissionCode code, String description, Instant createdAt) {
    this.id = Objects.requireNonNull(id, "Permission id cannot be null");
    this.code = Objects.requireNonNull(code, "Permission code cannot be null");
    this.description = normalizeDescription(description);
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
  }

  /**
   * Gets the unique identifier of this permission.
   * 
   * @return the permission ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the permission code.
   * 
   * @return the permission code (never null)
   */
  public PermissionCode code() {
    return code;
  }

  /**
   * Gets the optional description of this permission.
   * 
   * @return the description, or null if not set
   */
  public String description() {
    return description;
  }

  /**
   * Gets the timestamp when this permission was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Checks if this permission has the same code as another permission.
   * 
   * @param other the permission to compare with (never null)
   * @return true if both permissions have the same code, false otherwise
   * @throws NullPointerException if other is null
   */
  public boolean sameCodeAs(Permission other) {
    Objects.requireNonNull(other, "Permission cannot be null");
    return code == other.code;
  }

  /**
   * Validates and normalizes a permission description.
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
   * Compares this permission with another object for equality.
   * 
   * <p>Two permissions are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are permissions with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this permission based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
