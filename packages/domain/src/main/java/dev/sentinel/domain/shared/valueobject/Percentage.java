package dev.sentinel.domain.shared.valueobject;

public final class Percentage {

  private final int value;

  public Percentage(int value) {
    if (value < 0 || value > 100) {
      throw new IllegalArgumentException("Percentage must be between 0 and 100");
    }

    this.value = value;
  }

  public int value() {
    return value;
  }

  public boolean isZero() {
    return value == 0;
  }

  public boolean isHundred() {
    return value == 100;
  }

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

  @Override
  public int hashCode() {
    return Integer.hashCode(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
