package dev.sentinel.domain.application.model;

import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationTest {

  @Test
  void shouldCreateApplicationWithNormalizedFields() {
    Application application = createApplication(" Payments Service ", "  Main payments app  ");

    assertEquals("Payments Service", application.name());
    assertEquals("Main payments app", application.description());
    assertFalse(application.isArchived());
    assertTrue(application.isActive());
  }

  @Test
  void shouldRenameApplication() {
    Application application = createApplication("Payments", "Description");
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    application.rename(" Billing Service ", updatedAt);

    assertEquals("Billing Service", application.name());
    assertEquals(updatedAt, application.updatedAt());
  }

  @Test
  void shouldNormalizeDescriptionToNullWhenBlank() {
    Application application = createApplication("Payments", "Description");
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    application.changeDescription("   ", updatedAt);

    assertNull(application.description());
    assertEquals(updatedAt, application.updatedAt());
  }

  @Test
  void shouldArchiveAndRestoreApplication() {
    Application application = createApplication("Payments", "Description");
    Instant archivedAt = Instant.parse("2026-05-07T10:15:30Z");
    Instant restoredAt = Instant.parse("2026-05-07T11:15:30Z");

    application.archive(archivedAt);
    assertTrue(application.isArchived());
    assertFalse(application.isActive());
    assertEquals(archivedAt, application.updatedAt());

    application.restore(restoredAt);
    assertFalse(application.isArchived());
    assertTrue(application.isActive());
    assertEquals(restoredAt, application.updatedAt());
  }

  @Test
  void shouldRejectInvalidArchiveTransitions() {
    Application application = createApplication("Payments", "Description");
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    assertThrows(InvalidStateTransitionException.class, () -> application.restore(updatedAt));
    application.archive(updatedAt);
    assertThrows(InvalidStateTransitionException.class, () -> application.archive(updatedAt));
  }

  @Test
  void shouldRejectBlankApplicationName() {
    assertThrows(
        ValidationException.class,
        () -> createApplication("   ", "Description"));
  }

  private static Application createApplication(String name, String description) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new Application(
        UUID.randomUUID(),
        new ApplicationKey("payments-service"),
        name,
        description,
        UUID.randomUUID(),
        now,
        now);
  }
}
