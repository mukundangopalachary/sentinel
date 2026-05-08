package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.Objects;

/**
 * Immutable value object representing a unique user identifier.
 * 
 * UserIdentifier is used to uniquely identify end-users in external systems (e.g., customer IDs,
 * internal user UUIDs). It serves as a foreign key or reference to users in external systems.
 * This value object enforces validation at construction and is immutable thereafter.
 * 
 * @author Mukundan Gopalachary
 */
public final class UserIdentifier {

  /** The immutable user identifier value. */
  private final String value;

  /**
   * Constructs a new UserIdentifier with validation.
   * 
   * The provided identifier is trimmed but not further normalized. It must be non-blank.
   * 
   * @param userIdentifier the user identifier string (not null, will be trimmed)
   * @throws NullPointerException if userIdentifier is null
   * @throws ValidationException if the identifier is blank
   */
  public UserIdentifier(String userIdentifier) {
    Objects.requireNonNull(userIdentifier, "User identifier cannot be null");

    String normalized = userIdentifier.trim();

    if (normalized.isBlank()) {
      throw new ValidationException("User identifier cannot be blank");
    }

    this.value = normalized;
  }

  /**
   * Returns the immutable user identifier value.
   * 
   * @return the user identifier string
   */
  public String value() {
    return value;
  }

  /**
   * Checks equality of this user identifier with another object.
   * 
   * Two UserIdentifier instances are equal if they have the same underlying value.
   * 
   * @param obj the object to compare against
   * @return true if obj is a UserIdentifier with the same value, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof UserIdentifier)) {
      return false;
    }

    UserIdentifier other = (UserIdentifier) obj;
    return value.equals(other.value);
  }

  /**
   * Returns the hash code of this user identifier value.
   * 
   * Two UserIdentifier instances have the same hash code if their underlying values are equal.
   * 
   * @return the hash code of the identifier value
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /**
   * Returns the string representation of this user identifier.
   * 
   * @return the user identifier value as a string
   */
  @Override
  public String toString() {
    return value;
  }
}
