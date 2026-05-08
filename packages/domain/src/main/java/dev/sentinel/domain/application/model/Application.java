package dev.sentinel.domain.application.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a registered application in the feature flag management system.
 * 
 * <p>An Application is the top-level entity that owns environments and feature flags.
 * Applications can be created, renamed, archived, and restored by authorized users.
 * 
 * <p>Immutability: The id, key, createdBy, and createdAt fields are immutable. Other fields
 * (name, description, archived, updatedAt) can be modified through domain methods.
 * 
 * @author Sentinel Team
 */
public final class Application {

  private final UUID id;
  private final ApplicationKey key;
  private String name;
  private String description;
  private boolean archived;
  private final UUID createdBy;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new Application.
   * 
   * @param id the unique identifier for this application (never null)
   * @param key the application key used in URLs and APIs (never null)
   * @param name the human-readable name of the application (never blank)
   * @param description optional description of the application (can be null)
   * @param createdBy the user ID of the creator (never null)
   * @param createdAt the timestamp when the application was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   * @throws ValidationException if the name is blank or empty
   */
  public Application(
      UUID id,
      ApplicationKey key,
      String name,
      String description,
      UUID createdBy,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Application id cannot be null");
    this.key = Objects.requireNonNull(key, "Application key cannot be null");
    this.name = normalizeName(name);
    this.description = normalizeDescription(description);
    this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    this.archived = false;
  }

  /**
   * Gets the unique identifier of this application.
   * 
   * @return the application ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the application key used in URLs and APIs.
   * 
   * @return the application key (never null)
   */
  public ApplicationKey key() {
    return key;
  }

  /**
   * Gets the human-readable name of this application.
   * 
   * @return the application name (never blank)
   */
  public String name() {
    return name;
  }

  /**
   * Gets the optional description of this application.
   * 
   * @return the description, or null if not set
   */
  public String description() {
    return description;
  }

  /**
   * Checks if this application is archived.
   * 
   * @return true if archived, false otherwise
   */
  public boolean archived() {
    return archived;
  }

  /**
   * Gets the user ID of the creator of this application.
   * 
   * @return the creator's user ID (never null)
   */
  public UUID createdBy() {
    return createdBy;
  }

  /**
   * Gets the timestamp when this application was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this application.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Renames this application.
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
   * Updates the description of this application.
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
   * Archives this application, hiding it from active listings.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void archive(Instant updatedAt) {
    this.archived = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Restores this application from archived state.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void restore(Instant updatedAt) {
    this.archived = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this application is archived.
   * 
   * @return true if archived, false otherwise
   */
  public boolean isArchived() {
    return archived;
  }

  /**
   * Checks if this application is active (not archived).
   * 
   * @return true if active, false if archived
   */
  public boolean isActive() {
    return !archived;
  }

  /**
   * Validates and normalizes an application name.
   * 
   * <p>Trims whitespace and ensures the name is not blank.
   * 
   * @param name the name to normalize (must not be null)
   * @return the normalized name
   * @throws NullPointerException if name is null
   * @throws ValidationException if name is blank after trimming
   */
  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Application name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Application name cannot be blank");
    }

    return normalized;
  }

  /**
   * Validates and normalizes an application description.
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
   * Compares this application with another object for equality.
   * 
   * <p>Two applications are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are applications with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Application)) {
      return false;
    }

    Application other = (Application) obj;
    return id.equals(other.id);
  }

  /**
   * Computes a hash code for this application based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
