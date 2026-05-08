package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlagKeyTest {

  @Test
  void shouldCreateFlagKeyFromValidInput() {
    FlagKey flagKey = new FlagKey("new-checkout");

    assertEquals("new-checkout", flagKey.value());
  }

  @Test
  void shouldNormalizeFlagKey() {
    FlagKey flagKey = new FlagKey(" New-Checkout ");

    assertEquals("new-checkout", flagKey.value());
  }

  @Test
  void shouldRejectNullFlagKey() {
    assertThrows(NullPointerException.class, () -> new FlagKey(null));
  }

  @Test
  void shouldRejectBlankFlagKey() {
    assertThrows(ValidationException.class, () -> new FlagKey("   "));
  }

  @Test
  void shouldRejectInvalidFlagKeyFormat() {
    assertThrows(ValidationException.class, () -> new FlagKey("new_checkout"));
  }

  @Test
  void shouldCompareEqualNormalizedValues() {
    FlagKey first = new FlagKey("New-Checkout");
    FlagKey second = new FlagKey("new-checkout");

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}
