package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PercentageTest {

  @Test
  void shouldCreatePercentageWithinRange() {
    Percentage percentage = new Percentage(25);

    assertEquals(25, percentage.value());
  }

  @Test
  void shouldRejectPercentageBelowZero() {
    assertThrows(ValidationException.class, () -> new Percentage(-1));
  }

  @Test
  void shouldRejectPercentageAboveHundred() {
    assertThrows(ValidationException.class, () -> new Percentage(101));
  }

  @Test
  void shouldReportZeroAndHundredStates() {
    Percentage zero = new Percentage(0);
    Percentage hundred = new Percentage(100);

    assertTrue(zero.isZero());
    assertFalse(zero.isHundred());
    assertTrue(hundred.isHundred());
    assertFalse(hundred.isZero());
  }

  @Test
  void shouldCompareEqualValues() {
    Percentage first = new Percentage(50);
    Percentage second = new Percentage(50);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}
