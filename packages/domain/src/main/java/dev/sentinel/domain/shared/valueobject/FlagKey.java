package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Immutable value object representing a unique feature flag key.
 * 
 * Flag keys follow the pattern: lowercase alphanumeric characters with optional hyphens
 * (e.g., "new-checkout-flow", "beta-feature"). Keys are case-insensitive and automatically
 * normalized to lowercase. This value object enforces validation at construction and is
 * immutable thereafter.
 * 
 * @author Sentinel Team
 */
public final class FlagKey {

  private static final Pattern FLAG_KEY_PATTERN =
      Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  private final String value;

  /**
   * Constructs a new FlagKey with validation.
   * 
   * The provided key is normalized to lowercase and validated against the pattern:
   * lowercase alphanumeric with optional hyphens (e.g., "feature-x", "beta-testing").
   * 
   * @param flagKey the feature flag key string (not null, will be trimmed and lowercased)
   * @throws NullPointerException if flagKey is null
   * @throws ValidationException if the key is blank or does not match the required pattern
   */
  public FlagKey(String flagKey) {
    Objects.requireNonNull(flagKey, "Flag key cannot be null");

    String normalized = flagKey.trim().toLowerCase();

    if (normalized.isBlank()) {
      throw new ValidationException("Flag key cannot be blank");
    }

    if (!isValid(normalized)) {
      throw new ValidationException("Invalid flag key");
    }

    this.value = normalized;
  }

  /**
   * Validates the flag key format.
   * 
   * Checks if the key matches the pattern: lowercase alphanumeric characters with optional hyphens.
   * 
   * @param flagKey the key to validate
   * @return true if the key matches the required pattern, false otherwise
   */
  private static boolean isValid(String flagKey) {
    return FLAG_KEY_PATTERN.matcher(flagKey).matches();
  }

  /**
   * Returns the immutable feature flag key value.
   * 
   * @return the normalized (lowercase) flag key string
   */
  public String value() {
    return value;
  }

  /**
   * Checks equality of this flag key with another object.
   * 
   * Two FlagKey instances are equal if they have the same underlying value.
   * 
   * @param obj the object to compare against
   * @return true if obj is a FlagKey with the same value, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FlagKey)) {
      return false;
    }

    FlagKey other = (FlagKey) obj;
    return value.equals(other.value);
  }

  /**
   * Returns the hash code of this flag key value.
   * 
   * Two FlagKey instances have the same hash code if their underlying values are equal.
   * 
   * @return the hash code of the key value
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /**
   * Returns the string representation of this flag key.
   * 
   * @return the normalized key value as a string
   */
  @Override
  public String toString() {
    return value;
  }
}
