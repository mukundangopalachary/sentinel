package dev.sentinel.domain.environment.model;

import dev.sentinel.domain.shared.enums.EnvironmentType;
import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an environment within an application (e.g., dev, staging, prod).
 * 
 * <p>Environments allow organizations to manage feature flags separately for different
 * deployment targets. Each environment can have different flag configurations and can be
 * marked as protected to prevent accidental changes.
 * 
 * <p>Immutability: The id, applicationId, key, type, and createdAt fields are immutable.
 * Other fields can be modified through domain methods.
 * 
 * @author Mukundan Gopalachary
 */
public final class Environment {

  private final UUID id;
  private final UUID applicationId;
  private final EnvironmentKey key;
  private String name;
  private final EnvironmentType type;
  private boolean protectedEnvironment;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new Environment.
   * 
   * @param id the unique identifier for this environment (never null)
   * @param applicationId the ID of the parent application (never null)
   * @param key the environment key used in URLs and APIs (never null)
   * @param name the human-readable name of the environment (never blank)
   * @param type the environment type (dev, staging, prod, or custom; never null)
   * @param protectedEnvironment whether this environment is protected from accidental changes
   * @param createdAt the timestamp when the environment was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   * @throws ValidationException if name is blank or empty
   */
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

  /**
   * Gets the unique identifier of this environment.
   * 
   * @return the environment ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the parent application.
   * 
   * @return the application ID (never null)
   */
  public UUID applicationId() {
    return applicationId;
  }

  /**
   * Gets the environment key used in URLs and APIs.
   * 
   * @return the environment key (never null)
   */
  public EnvironmentKey key() {
    return key;
  }

  /**
   * Gets the human-readable name of this environment.
   * 
   * @return the environment name (never blank)
   */
  public String name() {
    return name;
  }

  /**
   * Gets the type of this environment.
   * 
   * @return the environment type (never null)
   */
  public EnvironmentType type() {
    return type;
  }

  /**
   * Checks if this environment is protected from accidental changes.
   * 
   * @return true if protected, false otherwise
   */
  public boolean protectedEnvironment() {
    return protectedEnvironment;
  }

  /**
   * Gets the timestamp when this environment was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this environment.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Renames this environment.
   * 
   * @param name the new name (must not be blank)
   * @param updatedAt the timestamp of the update (never null)
   * @throws ValidationException if name is blank or empty
   * @throws NullPointerException if updatedAt is null
   */
  public void rename(String name, Instant updatedAt) {
    this.name = normalizeName(name);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Marks this environment as protected from accidental changes.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void markProtected(Instant updatedAt) {
    if (protectedEnvironment) {
      throw new InvalidStateTransitionException("Environment is already protected");
    }
    this.protectedEnvironment = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Unmarks this environment from being protected.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void unmarkProtected(Instant updatedAt) {
    if (!protectedEnvironment) {
      throw new InvalidStateTransitionException("Environment is not protected");
    }
    this.protectedEnvironment = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this environment is protected.
   * 
   * @return true if protected, false otherwise
   */
  public boolean isProtected() {
    return protectedEnvironment;
  }

  /**
   * Checks if this environment is production-like (type is PROD).
   * 
   * @return true if this is a production environment, false otherwise
   */
  public boolean isProductionLike() {
    return type == EnvironmentType.PROD;
  }

  /**
   * Validates and normalizes an environment name.
   * 
   * <p>Trims whitespace and ensures the name is not blank.
   * 
   * @param name the name to normalize (must not be null)
   * @return the normalized name
   * @throws NullPointerException if name is null
   * @throws ValidationException if name is blank after trimming
   */
  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Environment name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Environment name cannot be blank");
    }

    return normalized;
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
   * Compares this environment with another object for equality.
   * 
   * <p>Two environments are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are environments with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this environment based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
