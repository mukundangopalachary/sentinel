package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicationKeyTest {

  @Test
  void shouldCreateApplicationKeyFromValidInput() {
    ApplicationKey applicationKey = new ApplicationKey("payments-service");

    assertEquals("payments-service", applicationKey.value());
  }

  @Test
  void shouldNormalizeApplicationKey() {
    ApplicationKey applicationKey = new ApplicationKey(" Payments-Service ");

    assertEquals("payments-service", applicationKey.value());
  }

  @Test
  void shouldRejectNullApplicationKey() {
    assertThrows(NullPointerException.class, () -> new ApplicationKey(null));
  }

  @Test
  void shouldRejectBlankApplicationKey() {
    assertThrows(ValidationException.class, () -> new ApplicationKey("   "));
  }

  @Test
  void shouldRejectInvalidApplicationKeyFormat() {
    assertThrows(ValidationException.class, () -> new ApplicationKey("payments_service"));
  }

  @Test
  void shouldCompareEqualNormalizedValues() {
    ApplicationKey first = new ApplicationKey("Payments-Service");
    ApplicationKey second = new ApplicationKey("payments-service");

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}
