package dev.sentinel.domain.environment.model;

import dev.sentinel.domain.shared.enums.EnvironmentType;
import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvironmentTest {

  @Test
  void shouldCreateEnvironment() {
    Environment environment = createEnvironment(" Production ", EnvironmentType.PROD, false);

    assertEquals("Production", environment.name());
    assertEquals(EnvironmentType.PROD, environment.type());
    assertFalse(environment.isProtected());
    assertTrue(environment.isProductionLike());
  }

  @Test
  void shouldRenameEnvironment() {
    Environment environment = createEnvironment("Staging", EnvironmentType.STAGING, false);
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    environment.rename(" QA ", updatedAt);

    assertEquals("QA", environment.name());
    assertEquals(updatedAt, environment.updatedAt());
  }

  @Test
  void shouldMarkAndUnmarkProtectedEnvironment() {
    Environment environment = createEnvironment("Prod", EnvironmentType.PROD, false);
    Instant protectedAt = Instant.parse("2026-05-07T10:15:30Z");
    Instant unprotectedAt = Instant.parse("2026-05-07T11:15:30Z");

    environment.markProtected(protectedAt);
    assertTrue(environment.isProtected());
    assertEquals(protectedAt, environment.updatedAt());

    environment.unmarkProtected(unprotectedAt);
    assertFalse(environment.isProtected());
    assertEquals(unprotectedAt, environment.updatedAt());
  }

  @Test
  void shouldRejectInvalidProtectionTransitions() {
    Environment environment = createEnvironment("Prod", EnvironmentType.PROD, false);
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    assertThrows(InvalidStateTransitionException.class, () -> environment.unmarkProtected(updatedAt));
    environment.markProtected(updatedAt);
    assertThrows(InvalidStateTransitionException.class, () -> environment.markProtected(updatedAt));
  }

  @Test
  void shouldRecognizeNonProductionEnvironment() {
    Environment environment = createEnvironment("Staging", EnvironmentType.STAGING, false);

    assertFalse(environment.isProductionLike());
  }

  @Test
  void shouldRejectBlankEnvironmentName() {
    assertThrows(
        ValidationException.class,
        () -> createEnvironment("   ", EnvironmentType.DEV, false));
  }

  private static Environment createEnvironment(
      String name, EnvironmentType type, boolean protectedEnvironment) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new Environment(
        UUID.randomUUID(),
        UUID.randomUUID(),
        new EnvironmentKey("prod"),
        name,
        type,
        protectedEnvironment,
        now,
        now);
  }
}
