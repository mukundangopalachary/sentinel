package dev.sentinel.domain.role.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RolePermissionTest {

  @Test
  void shouldMatchRoleAndPermissionPair() {
    UUID roleId = UUID.randomUUID();
    UUID permissionId = UUID.randomUUID();
    RolePermission rolePermission =
        new RolePermission(UUID.randomUUID(), roleId, permissionId, Instant.parse("2026-05-08T09:15:30Z"));

    assertTrue(rolePermission.matches(roleId, permissionId));
    assertFalse(rolePermission.matches(UUID.randomUUID(), permissionId));
  }
}
