package dev.sentinel.domain.role.model;

import dev.sentinel.domain.shared.enums.PermissionCode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionTest {

  @Test
  void shouldCreatePermissionAndNormalizeDescription() {
    Permission permission = createPermission(PermissionCode.FLAG_UPDATE, "  Update a flag  ");

    assertEquals(PermissionCode.FLAG_UPDATE, permission.code());
    assertEquals("Update a flag", permission.description());
  }

  @Test
  void shouldTreatBlankDescriptionAsNull() {
    Permission permission = createPermission(PermissionCode.FLAG_UPDATE, "   ");

    assertNull(permission.description());
  }

  @Test
  void shouldComparePermissionsByCode() {
    Permission first = createPermission(PermissionCode.FLAG_UPDATE, "Update");
    Permission second = createPermission(PermissionCode.FLAG_UPDATE, "Also update");
    Permission third = createPermission(PermissionCode.FLAG_DELETE, "Delete");

    assertTrue(first.sameCodeAs(second));
    assertFalse(first.sameCodeAs(third));
  }

  private static Permission createPermission(PermissionCode code, String description) {
    return new Permission(UUID.randomUUID(), code, description, Instant.parse("2026-05-08T09:15:30Z"));
  }
}
