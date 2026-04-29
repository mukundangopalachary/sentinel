package dev.sentinel.domain.shared.valueobject;

import java.util.Objects;

public final class TokenHash {

  private final String value;

  public TokenHash(String tokenHash) {
    Objects.requireNonNull(tokenHash, "Token hash cannot be null");

    String normalized = tokenHash.trim();

    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Token hash cannot be blank");
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
    if (!(obj instanceof TokenHash)) {
      return false;
    }

    TokenHash other = (TokenHash) obj;
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
