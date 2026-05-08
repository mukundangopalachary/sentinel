package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

  @Test
  void shouldCreateEmailFromValidInput() {
    Email email = new Email("user@example.com");

    assertEquals("user@example.com", email.value());
  }

  @Test
  void shouldNormalizeEmail() {
    Email email = new Email(" User@Example.COM ");

    assertEquals("user@example.com", email.value());
  }

  @Test
  void shouldRejectNullEmail() {
    assertThrows(NullPointerException.class, () -> new Email(null));
  }

  @Test
  void shouldRejectBlankEmail() {
    assertThrows(ValidationException.class, () -> new Email("   "));
  }

  @Test
  void shouldRejectInvalidEmailFormat() {
    assertThrows(ValidationException.class, () -> new Email("not-an-email"));
  }

  @Test
  void shouldCompareEqualNormalizedValues() {
    Email first = new Email("User@Example.COM");
    Email second = new Email("user@example.com");

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}
