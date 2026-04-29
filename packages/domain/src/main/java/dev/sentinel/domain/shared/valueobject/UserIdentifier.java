package dev.sentinel.domain.shared.valueobject;

import java.util.Objects;

public final class UserIdentifier {

  private final String value;

  public UserIdentifier(String userIdentifier) {
    Objects.requireNonNull(userIdentifier, "User identifier cannot be null");

    String normalized = userIdentifier.trim();

    if (normalized.isBlank()) {
      throw new IllegalArgumentException("User identifier cannot be blank");
    }

    this.value = normalized;
  }

  public String value() {
    return value;
  }

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

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
