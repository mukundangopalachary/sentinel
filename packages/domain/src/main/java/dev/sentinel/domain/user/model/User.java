package dev.sentinel.domain.user.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.Email;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class User {

  private final UUID id;
  private final Email email;
  private String passwordHash;
  private String displayName;
  private boolean active;
  private Instant lastLoginAt;
  private final Instant createdAt;
  private Instant updatedAt;

  public User(
      UUID id,
      Email email,
      String passwordHash,
      String displayName,
      boolean active,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "User id cannot be null");
    this.email = Objects.requireNonNull(email, "Email cannot be null");
    this.passwordHash = normalizePasswordHash(passwordHash);
    this.displayName = normalizeDisplayName(displayName);
    this.active = active;
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public Email email() {
    return email;
  }

  public String passwordHash() {
    return passwordHash;
  }

  public String displayName() {
    return displayName;
  }

  public boolean active() {
    return active;
  }

  public Instant lastLoginAt() {
    return lastLoginAt;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant updatedAt() {
    return updatedAt;
  }

  public void activate(Instant updatedAt) {
    this.active = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void deactivate(Instant updatedAt) {
    this.active = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void recordLogin(Instant loginAt, Instant updatedAt) {
    this.lastLoginAt = Objects.requireNonNull(loginAt, "Login time cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void changeDisplayName(String displayName, Instant updatedAt) {
    this.displayName = normalizeDisplayName(displayName);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void changePassword(String passwordHash, Instant updatedAt) {
    this.passwordHash = normalizePasswordHash(passwordHash);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public boolean isEligibleForLogin() {
    return active;
  }

  private static String normalizePasswordHash(String passwordHash) {
    Objects.requireNonNull(passwordHash, "Password hash cannot be null");

    String normalized = passwordHash.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Password hash cannot be blank");
    }

    return normalized;
  }

  private static String normalizeDisplayName(String displayName) {
    Objects.requireNonNull(displayName, "Display name cannot be null");

    String normalized = displayName.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Display name cannot be blank");
    }

    return normalized;
  }

  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof User)) {
      return false;
    }

    User other = (User) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
