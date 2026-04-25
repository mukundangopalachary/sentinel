package dev.sentinel.domain.shared.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public final class FlagKey {

  private static final Pattern FLAG_KEY_PATTERN =
      Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  private final String value;

  public FlagKey(String flagKey) {
    Objects.requireNonNull(flagKey, "Flag key cannot be null");

    String normalized = flagKey.trim().toLowerCase();

    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Flag key cannot be blank");
    }

    if (!isValid(normalized)) {
      throw new IllegalArgumentException("Invalid flag key");
    }

    this.value = normalized;
  }

  private static boolean isValid(String flagKey) {
    return FLAG_KEY_PATTERN.matcher(flagKey).matches();
  }

  public String value() {
    return value;
  }

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

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
