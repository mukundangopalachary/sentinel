package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.Objects;

/**
 * Immutable value object representing a hashed token value.
 * 
 * TokenHash stores the securely hashed representation of an authentication token. The raw token
 * is never stored; only the hash is kept. This value object enforces validation at construction
 * and is immutable thereafter.
 * 
 * @author Mukundan Gopalachary
 */
public final class TokenHash {

  /** The immutable hashed token value. */
  private final String value;

  /**
   * Constructs a new TokenHash with validation.
   * 
   * The provided hash is trimmed but not further normalized. It must be non-blank.
   * 
   * @param tokenHash the hashed token string (not null, will be trimmed)
   * @throws NullPointerException if tokenHash is null
   * @throws ValidationException if the hash is blank
   */
  public TokenHash(String tokenHash) {
    Objects.requireNonNull(tokenHash, "Token hash cannot be null");

    String normalized = tokenHash.trim();

    if (normalized.isBlank()) {
      throw new ValidationException("Token hash cannot be blank");
    }

    this.value = normalized;
  }

  /**
   * Returns the immutable token hash value.
   * 
   * @return the hashed token string
   */
  public String value() {
    return value;
  }

  /**
   * Checks equality of this token hash with another object.
   * 
   * Two TokenHash instances are equal if they have the same underlying hash value.
   * 
   * @param obj the object to compare against
   * @return true if obj is a TokenHash with the same value, false otherwise
   */
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

  /**
   * Returns the hash code of this token hash value.
   * 
   * Two TokenHash instances have the same hash code if their underlying values are equal.
   * 
   * @return the hash code of the token hash value
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  /**
   * Returns the string representation of this token hash.
   * 
   * @return the token hash value as a string
   */
  @Override
  public String toString() {
    return value;
  }
}
