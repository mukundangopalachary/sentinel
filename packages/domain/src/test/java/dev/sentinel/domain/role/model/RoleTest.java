package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.RoleType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleTest {

  @Test
  void shouldCreateRoleAndNormalizeDescription() {
    Role role = createRole("  Application owner  ", false);

    assertEquals(RoleType.OWNER, role.name());
    assertEquals("Application owner", role.description());
  }

  @Test
  void shouldChangeDescriptionAndMarkAsSystemRole() {
    Role role = createRole("Owner", false);
    Instant updatedAt = Instant.parse("2026-05-08T10:15:30Z");

    role.changeDescription("  Platform owner  ", updatedAt);
    role.markAsSystemRole(updatedAt);

    assertEquals("Platform owner", role.description());
    assertTrue(role.systemRole());
    assertTrue(role.isAssignable());
  }

  @Test
  void shouldNormalizeBlankDescriptionToNull() {
    Role role = createRole("Owner", false);
    Instant updatedAt = Instant.parse("2026-05-08T10:15:30Z");

    role.changeDescription("   ", updatedAt);

    assertNull(role.description());
  }

  private static Role createRole(String description, boolean systemRole) {
    Instant now = Instant.parse("2026-05-08T09:15:30Z");
    return new Role(UUID.randomUUID(), RoleType.OWNER, description, systemRole, now, now);
  }
}
