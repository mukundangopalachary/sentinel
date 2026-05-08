package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

/**
 * Immutable value object representing a percentage in the range [0, 100].
 * 
 * Percentage is used for flag rollout calculations and feature distribution. The value is
 * always between 0 and 100 (inclusive). This value object enforces validation at construction
 * and is immutable thereafter.
 * 
 * @author Mukundan Gopalachary
 */
public final class Percentage {

  private final int value;

  /**
   * Constructs a new Percentage with validation.
   * 
   * @param value the percentage value (must be between 0 and 100, inclusive)
   * @throws ValidationException if value is less than 0 or greater than 100
   */
  public Percentage(int value) {
    if (value < 0 || value > 100) {
      throw new ValidationException("Percentage must be between 0 and 100");
    }

    this.value = value;
  }

  /**
   * Returns the immutable percentage value.
   * 
   * @return the percentage as an integer between 0 and 100
   */
  public int value() {
    return value;
  }

  /**
   * Checks if this percentage is zero (0%).
   * 
   * @return true if the percentage value is 0, false otherwise
   */
  public boolean isZero() {
    return value == 0;
  }

  /**
   * Checks if this percentage is one hundred (100%).
   * 
   * @return true if the percentage value is 100, false otherwise
   */
  public boolean isHundred() {
    return value == 100;
  }

  /**
   * Checks equality of this percentage with another object.
   * 
   * Two Percentage instances are equal if they have the same numeric value.
   * 
   * @param obj the object to compare against
   * @return true if obj is a Percentage with the same value, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Percentage)) {
      return false;
    }

    Percentage other = (Percentage) obj;
    return value == other.value;
  }

  /**
   * Returns the hash code of this percentage value.
   * 
   * Two Percentage instances have the same hash code if their values are equal.
   * 
   * @return the hash code of the percentage value
   */
  @Override
  public int hashCode() {
    return Integer.hashCode(value);
  }

  /**
   * Returns the string representation of this percentage.
   * 
   * @return the percentage value as a string (e.g., "0", "50", "100")
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
