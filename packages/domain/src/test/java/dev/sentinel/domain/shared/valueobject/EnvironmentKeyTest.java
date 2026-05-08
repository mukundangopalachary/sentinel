package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnvironmentKeyTest {

  @Test
  void shouldCreateEnvironmentKeyFromValidInput() {
    EnvironmentKey environmentKey = new EnvironmentKey("staging");

    assertEquals("staging", environmentKey.value());
  }

  @Test
  void shouldNormalizeEnvironmentKey() {
    EnvironmentKey environmentKey = new EnvironmentKey(" Prod ");

    assertEquals("prod", environmentKey.value());
  }

  @Test
  void shouldRejectNullEnvironmentKey() {
    assertThrows(NullPointerException.class, () -> new EnvironmentKey(null));
  }

  @Test
  void shouldRejectBlankEnvironmentKey() {
    assertThrows(ValidationException.class, () -> new EnvironmentKey("   "));
  }

  @Test
  void shouldRejectInvalidEnvironmentKeyFormat() {
    assertThrows(ValidationException.class, () -> new EnvironmentKey("qa_env"));
  }

  @Test
  void shouldCompareEqualNormalizedValues() {
    EnvironmentKey first = new EnvironmentKey("Staging");
    EnvironmentKey second = new EnvironmentKey("staging");

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }
}
