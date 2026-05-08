package dev.sentinel.domain.audit.model;

import dev.sentinel.domain.shared.enums.AuditAction;
import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditLogTest {

  @Test
  void shouldCreateAuditLogAndNormalizeOptionalFields() {
    UUID entityId = UUID.randomUUID();
    AuditLog auditLog =
        createAuditLogWithMetadata("  FEATURE_FLAG  ", entityId, " 127.0.0.1 ", "  browser  ");

    assertEquals("FEATURE_FLAG", auditLog.entityType());
    assertEquals("127.0.0.1", auditLog.ipAddress());
    assertEquals("browser", auditLog.userAgent());
    assertTrue(auditLog.isForEntity("FEATURE_FLAG", entityId));
  }

  @Test
  void shouldReportWhetherChangeWasCaptured() {
    AuditLog withChange =
        createAuditLogWithChange("FLAG", UUID.randomUUID(), "{ \"old\": 1 }", "{ \"new\": 2 }");
    AuditLog withoutChange =
        createAuditLogWithChange("FLAG", UUID.randomUUID(), "   ", "   ");

    assertTrue(withChange.capturesChange());
    assertFalse(withoutChange.capturesChange());
    assertNull(withoutChange.oldValueJson());
    assertNull(withoutChange.newValueJson());
  }

  @Test
  void shouldRejectBlankEntityType() {
    assertThrows(
        ValidationException.class,
        () -> createAuditLogWithChange("   ", UUID.randomUUID(), "{ \"old\": 1 }", "{ \"new\": 2 }"));
  }

  private static AuditLog createAuditLogWithChange(
      String entityType, UUID entityId, String oldValueJson, String newValueJson) {
    return new AuditLog(
        UUID.randomUUID(),
        UUID.randomUUID(),
        entityType,
        entityId,
        AuditAction.UPDATE,
        oldValueJson,
        newValueJson,
        null,
        null,
        Instant.parse("2026-05-08T09:15:30Z"));
  }

  private static AuditLog createAuditLogWithMetadata(
      String entityType, UUID entityId, String ipAddress, String userAgent) {
    return new AuditLog(
        UUID.randomUUID(),
        UUID.randomUUID(),
        entityType,
        entityId,
        AuditAction.UPDATE,
        "{ \"old\": 1 }",
        "{ \"new\": 2 }",
        ipAddress,
        userAgent,
        Instant.parse("2026-05-08T09:15:30Z"));
  }
}
