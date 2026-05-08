package dev.sentinel.domain.user.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.Email;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

  @Test
  void shouldCreateUserWithNormalizedFields() {
    User user = createUser(" User Name ", "  hash  ", true);

    assertEquals("User Name", user.displayName());
    assertEquals("hash", user.passwordHash());
    assertTrue(user.active());
    assertTrue(user.isEligibleForLogin());
  }

  @Test
  void shouldActivateDeactivateAndRecordLogin() {
    User user = createUser("User", "hash", false);
    Instant activatedAt = Instant.parse("2026-05-08T10:15:30Z");
    Instant loginAt = Instant.parse("2026-05-08T11:15:30Z");
    Instant updatedAt = Instant.parse("2026-05-08T11:16:30Z");
    Instant deactivatedAt = Instant.parse("2026-05-08T12:15:30Z");

    user.activate(activatedAt);
    assertTrue(user.active());

    user.recordLogin(loginAt, updatedAt);
    assertEquals(loginAt, user.lastLoginAt());
    assertEquals(updatedAt, user.updatedAt());

    user.deactivate(deactivatedAt);
    assertFalse(user.active());
    assertFalse(user.isEligibleForLogin());
  }

  @Test
  void shouldChangeDisplayNameAndPassword() {
    User user = createUser("User", "hash", true);
    Instant updatedAt = Instant.parse("2026-05-08T10:15:30Z");

    user.changeDisplayName(" Another User ", updatedAt);
    user.changePassword("  next-hash  ", updatedAt);

    assertEquals("Another User", user.displayName());
    assertEquals("next-hash", user.passwordHash());
  }

  @Test
  void shouldRejectBlankDisplayName() {
    assertThrows(ValidationException.class, () -> createUser("   ", "hash", true));
  }

  private static User createUser(String displayName, String passwordHash, boolean active) {
    Instant now = Instant.parse("2026-05-08T09:15:30Z");
    return new User(
        UUID.randomUUID(),
        new Email("user@example.com"),
        passwordHash,
        displayName,
        active,
        now,
        now);
  }
}
