package dev.sentinel.domain.token.model;

import dev.sentinel.domain.shared.enums.TokenStatus;
import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.TokenHash;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiceTokenTest {

  @Test
  void shouldCreateServiceToken() {
    ServiceToken token = createToken("  SDK Token  ", TokenStatus.ACTIVE, Instant.parse("2026-05-09T09:15:30Z"));

    assertEquals("SDK Token", token.name());
    assertEquals(TokenStatus.ACTIVE, token.status());
  }

  @Test
  void shouldMarkUsedAndRevokeToken() {
    ServiceToken token = createToken("SDK Token", TokenStatus.ACTIVE, Instant.parse("2026-05-09T09:15:30Z"));
    Instant usedAt = Instant.parse("2026-05-08T10:15:30Z");
    Instant revokedAt = Instant.parse("2026-05-08T11:15:30Z");

    token.markUsed(usedAt);
    assertEquals(usedAt, token.lastUsedAt());

    token.revoke(revokedAt);
    assertEquals(TokenStatus.REVOKED, token.status());
    assertEquals(revokedAt, token.revokedAt());
  }

  @Test
  void shouldRejectRevokingAlreadyRevokedToken() {
    ServiceToken token =
        createToken("SDK Token", TokenStatus.REVOKED, Instant.parse("2026-05-09T09:15:30Z"));

    assertThrows(
        InvalidStateTransitionException.class,
        () -> token.revoke(Instant.parse("2026-05-08T11:15:30Z")));
  }

  @Test
  void shouldEvaluateActiveAndExpiredStates() {
    Instant expiresAt = Instant.parse("2026-05-09T09:15:30Z");
    ServiceToken token = createToken("SDK Token", TokenStatus.ACTIVE, expiresAt);

    assertTrue(token.isActive(Instant.parse("2026-05-08T10:15:30Z")));
    assertFalse(token.isExpired(Instant.parse("2026-05-08T10:15:30Z")));
    assertTrue(token.isExpired(Instant.parse("2026-05-10T10:15:30Z")));
    assertFalse(token.isActive(Instant.parse("2026-05-10T10:15:30Z")));
  }

  @Test
  void shouldRejectBlankName() {
    assertThrows(
        ValidationException.class,
        () -> createToken("   ", TokenStatus.ACTIVE, Instant.parse("2026-05-09T09:15:30Z")));
  }

  private static ServiceToken createToken(String name, TokenStatus status, Instant expiresAt) {
    return new ServiceToken(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        name,
        new TokenHash("hash-value"),
        status,
        expiresAt,
        null,
        UUID.randomUUID(),
        Instant.parse("2026-05-08T09:15:30Z"),
        null);
  }
}
