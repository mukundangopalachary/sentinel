package dev.sentinel.domain.application.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Application {

  private final UUID id;
  private final ApplicationKey key;
  private String name;
  private String description;
  private boolean archived;
  private final UUID createdBy;
  private final Instant createdAt;
  private Instant updatedAt;

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

  public UUID id() {
    return id;
  }

  public ApplicationKey key() {
    return key;
  }

  public String name() {
    return name;
  }

  public String description() {
    return description;
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

  public boolean isActive() {
    return !archived;
  }

  private static String normalizeName(String name) {
    Objects.requireNonNull(name, "Application name cannot be null");

    String normalized = name.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Application name cannot be blank");
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
    if (!(obj instanceof Application)) {
      return false;
    }

    Application other = (Application) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
