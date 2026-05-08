package dev.sentinel.domain.featureflag.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a feature flag within an environment of an application.
 * 
 * <p>Feature flags are the core entities that can be enabled or disabled at runtime
 * to control application behavior without redeployment. Each flag has rules that
 * determine whether it evaluates to true or false for a given user or context.
 * 
 * <p>Immutability: The id, applicationId, environmentId, key, createdBy, and createdAt
 * fields are immutable. Other fields can be modified through domain methods.
 * 
 * @author Mukundan Gopalachary
 */
public final class FeatureFlag {

  private final UUID id;
  private final UUID applicationId;
  private final UUID environmentId;
  private final FlagKey key;
  private String name;
  private String description;
  private boolean enabled;
  private boolean archived;
  private final UUID createdBy;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new FeatureFlag.
   * 
   * @param id the unique identifier for this flag (never null)
   * @param applicationId the ID of the parent application (never null)
   * @param environmentId the ID of the parent environment (never null)
   * @param key the flag key used in runtime evaluations (never null)
   * @param name the human-readable name of the flag (never blank)
   * @param description optional description of the flag (can be null)
   * @param enabled whether the flag is initially enabled or disabled
   * @param createdBy the user ID of the creator (never null)
   * @param createdAt the timestamp when the flag was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   * @throws ValidationException if name is blank or empty
   */
  public FeatureFlag(
      UUID id,
      UUID applicationId,
      UUID environmentId,
      FlagKey key,
      String name,
      String description,
      boolean enabled,
      UUID createdBy,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Feature flag id cannot be null");
    this.applicationId = Objects.requireNonNull(applicationId, "Application id cannot be null");
    this.environmentId = Objects.requireNonNull(environmentId, "Environment id cannot be null");
    this.key = Objects.requireNonNull(key, "Flag key cannot be null");
    this.name = normalizeName(name);
    this.description = normalizeDescription(description);
    this.enabled = enabled;
    this.archived = false;
    this.createdBy = Objects.requireNonNull(createdBy, "Created by cannot be null");
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  /**
   * Gets the unique identifier of this feature flag.
   * 
   * @return the flag ID (never null)
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
   * Gets the ID of the parent environment.
   * 
   * @return the environment ID (never null)
   */
  public UUID environmentId() {
    return environmentId;
  }

  /**
   * Gets the flag key used in runtime evaluations.
   * 
   * @return the flag key (never null)
   */
  public FlagKey key() {
    return key;
  }

  /**
   * Gets the human-readable name of this feature flag.
   * 
   * @return the flag name (never blank)
   */
  public String name() {
    return name;
  }

  /**
   * Gets the optional description of this feature flag.
   * 
   * @return the description, or null if not set
   */
  public String description() {
    return description;
  }

  /**
   * Checks if this feature flag is currently enabled.
   * 
   * @return true if enabled, false otherwise
   */
  public boolean enabled() {
    return enabled;
  }

  /**
   * Checks if this feature flag is archived.
   * 
   * @return true if archived, false otherwise
   */
  public boolean archived() {
    return archived;
  }

  /**
   * Gets the user ID of the creator of this feature flag.
   * 
   * @return the creator's user ID (never null)
   */
  public UUID createdBy() {
    return createdBy;
  }

  /**
   * Gets the timestamp when this feature flag was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this feature flag.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Renames this feature flag.
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
   * Updates the description of this feature flag.
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
   * Enables this feature flag.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void enable(Instant updatedAt) {
    this.enabled = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Disables this feature flag.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void disable(Instant updatedAt) {
    this.enabled = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Archives this feature flag.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void archive(Instant updatedAt) {
    this.archived = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Restores this feature flag from archived state.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void restore(Instant updatedAt) {
    this.archived = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this feature flag is archived.
   * 
   * @return true if archived, false otherwise
   */
  public boolean isArchived() {
    return archived;
  }

  /**
   * Checks if this feature flag is evaluable (enabled and not archived).
   * 
   * @return true if the flag can be evaluated, false otherwise
   */
  public boolean isEvaluable() {
    return enabled && !archived;
  }

  /**
   * Validates and normalizes a feature flag name.
   * 
   * <p>Trims whitespace and ensures the name is not blank.
   * 
   * @param name the name to normalize (must not be null)
   * @return the normalized name
   * @throws NullPointerException if name is null
   * @throws ValidationException if name is blank after trimming
   */
  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Feature flag name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Feature flag name cannot be blank");
    }

    return normalized;
  }

  /**
   * Validates and normalizes a feature flag description.
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
   * Compares this feature flag with another object for equality.
   * 
   * <p>Two feature flags are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are feature flags with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FeatureFlag)) {
      return false;
    }

    FeatureFlag other = (FeatureFlag) obj;
    return id.equals(other.id);
  }

  /**
   * Computes a hash code for this feature flag based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
