package dev.sentinel.domain.user.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.Email;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user in the feature flag management system.
 * 
 * <p>Users are authenticated principals who can manage applications, environments,
 * flags, and other system resources. Users can be activated or deactivated, and
 * their login activity is tracked for audit purposes.
 * 
 * <p>Immutability: The id, email, and createdAt fields are immutable. Other fields
 * can be modified through domain methods.
 * 
 * @author Sentinel Team
 */
public final class User {

  private final UUID id;
  private final Email email;
  private String passwordHash;
  private String displayName;
  private boolean active;
  private Instant lastLoginAt;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new User.
   * 
   * @param id the unique identifier for this user (never null)
   * @param email the user's email address (never null, must be valid)
   * @param passwordHash the bcrypt/scrypt hash of the user's password (never blank)
   * @param displayName the user's display name (never blank)
   * @param active whether the user account is active and can login
   * @param createdAt the timestamp when the user was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   * @throws ValidationException if passwordHash or displayName is blank
   */
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

  /**
   * Gets the unique identifier of this user.
   * 
   * @return the user ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the email address of this user.
   * 
   * @return the email (never null)
   */
  public Email email() {
    return email;
  }

  /**
   * Gets the password hash for this user.
   * 
   * @return the password hash (never blank)
   */
  public String passwordHash() {
    return passwordHash;
  }

  /**
   * Gets the display name of this user.
   * 
   * @return the display name (never blank)
   */
  public String displayName() {
    return displayName;
  }

  /**
   * Checks if this user account is active.
   * 
   * @return true if active, false if deactivated
   */
  public boolean active() {
    return active;
  }

  /**
   * Gets the timestamp of the user's last login.
   * 
   * @return the last login timestamp, or null if never logged in
   */
  public Instant lastLoginAt() {
    return lastLoginAt;
  }

  /**
   * Gets the timestamp when this user was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this user.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Activates this user account, allowing them to login.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void activate(Instant updatedAt) {
    this.active = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Deactivates this user account, preventing them from logging in.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void deactivate(Instant updatedAt) {
    this.active = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Records a successful login for this user.
   * 
   * @param loginAt the timestamp of the login (never null)
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if either parameter is null
   */
  public void recordLogin(Instant loginAt, Instant updatedAt) {
    this.lastLoginAt = Objects.requireNonNull(loginAt, "Login time cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Changes the display name of this user.
   * 
   * @param displayName the new display name (must not be blank)
   * @param updatedAt the timestamp of the update (never null)
   * @throws ValidationException if displayName is blank or empty
   * @throws NullPointerException if updatedAt is null
   */
  public void changeDisplayName(String displayName, Instant updatedAt) {
    this.displayName = normalizeDisplayName(displayName);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Changes the password hash for this user.
   * 
   * @param passwordHash the new password hash (must not be blank)
   * @param updatedAt the timestamp of the update (never null)
   * @throws ValidationException if passwordHash is blank or empty
   * @throws NullPointerException if updatedAt is null
   */
  public void changePassword(String passwordHash, Instant updatedAt) {
    this.passwordHash = normalizePasswordHash(passwordHash);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this user is eligible to login.
   * 
   * @return true if the user is active, false otherwise
   */
  public boolean isEligibleForLogin() {
    return active;
  }

  /**
   * Validates and normalizes a password hash.
   * 
   * <p>Trims whitespace and ensures the hash is not blank.
   * 
   * @param passwordHash the hash to normalize (must not be null)
   * @return the normalized hash
   * @throws NullPointerException if passwordHash is null
   * @throws ValidationException if passwordHash is blank after trimming
   */
  private static String normalizePasswordHash(String passwordHash) {
    Objects.requireNonNull(passwordHash, "Password hash cannot be null");

    String normalized = passwordHash.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Password hash cannot be blank");
    }

    return normalized;
  }

  /**
   * Validates and normalizes a display name.
   * 
   * <p>Trims whitespace and ensures the name is not blank.
   * 
   * @param displayName the name to normalize (must not be null)
   * @return the normalized name
   * @throws NullPointerException if displayName is null
   * @throws ValidationException if displayName is blank after trimming
   */
  private static String normalizeDisplayName(String displayName) {
    Objects.requireNonNull(displayName, "Display name cannot be null");

    String normalized = displayName.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Display name cannot be blank");
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
   * Compares this user with another object for equality.
   * 
   * <p>Two users are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are users with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this user based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
