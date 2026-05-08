package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Represents a validated application key value object.
 * 
 * <p>An application key is a unique, URL-safe identifier for an application.
 * Keys must follow the pattern: lowercase alphanumeric characters with optional hyphens
 * (e.g., "my-app", "app-v2").
 * 
 * <p>This is a value object: equality is based on the key value, not object identity.
 * 
 * @author Sentinel Team
 */
public final class ApplicationKey {

  /** Pattern for valid application keys: lowercase alphanumeric with hyphens */
  private static final Pattern APPLICATION_KEY_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  private final String value;

  /**
   * Constructs a new ApplicationKey.
   * 
   * @param applicationKey the key to validate and store (must not be null)
   * @throws NullPointerException if applicationKey is null
   * @throws ValidationException if applicationKey is blank or does not match the required pattern
   */
  public ApplicationKey(String applicationKey) {

    Objects.requireNonNull(applicationKey, "Application key cannot be null");

    String normalized = applicationKey.trim().toLowerCase();

    if (normalized.isBlank()) {
      throw new ValidationException("Application key cannot be blank");
    }

    if (!isValid(normalized)) {
      throw new ValidationException("Invalid application key");
    }

    this.value = normalized;
  }

  /**
   * Validates the key against the required pattern.
   * 
   * @param applicationKey the key to validate
   * @return true if valid, false otherwise
   */
  private static boolean isValid(String applicationKey) {
    return APPLICATION_KEY_PATTERN.matcher(applicationKey).matches();
  }

  /**
   * Gets the key value.
   * 
   * @return the key value (never null)
   */
  public String value() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof ApplicationKey))
      return false;
    ApplicationKey other = (ApplicationKey) obj;
    return this.value.equals(other.value);
  }

  @Override
  public String toString() {
    return value;
  }
}
