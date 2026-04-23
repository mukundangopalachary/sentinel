package dev.sentinel.domain.shared.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class ApplicationKey {

  // Application key regex
  private static final Pattern APPLICATION_KEY_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  // value
  private final String value;

  // constructor
  public ApplicationKey(String applicationKey) {

    // check if application key is null
    Objects.requireNonNull(applicationKey, "Application key cannot be null");

    // proper format
    String normalized = applicationKey.trim().toLowerCase();

    // if blank space present, throw error
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Application key cannot be blank");
    }

    // check if application key is following the valid pattern
    if (!isValid(normalized)) {
      throw new IllegalArgumentException("Invalid Application key");
    }

    this.value = normalized;
  }

  // validate the pattern
  private static boolean isValid(String applicationKey) {
    return APPLICATION_KEY_PATTERN.matcher(applicationKey).matches();
  }

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

  public String toString() {
    return value;
  }
}
