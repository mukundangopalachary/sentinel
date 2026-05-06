package dev.sentinel.domain.featureflag.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

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

  public UUID id() {
    return id;
  }

  public UUID applicationId() {
    return applicationId;
  }

  public UUID environmentId() {
    return environmentId;
  }

  public FlagKey key() {
    return key;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
  }

  public boolean enabled() {
    return enabled;
  }

  public boolean archived() {
    return archived;
  }

  public UUID createdBy() {
    return createdBy;
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

  public void changeDescription(String description, Instant updatedAt) {
    this.description = normalizeDescription(description);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void enable(Instant updatedAt) {
    this.enabled = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void disable(Instant updatedAt) {
    this.enabled = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void archive(Instant updatedAt) {
    this.archived = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void restore(Instant updatedAt) {
    this.archived = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public boolean isArchived() {
    return archived;
  }

  public boolean isEvaluable() {
    return enabled && !archived;
  }

  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Feature flag name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Feature flag name cannot be blank");
    }

    return normalized;
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
    if (!(obj instanceof FeatureFlag)) {
      return false;
    }

    FeatureFlag other = (FeatureFlag) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
