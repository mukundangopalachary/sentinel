package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.valueobject.FlagKey;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluationResultTest {

  @Test
  void shouldRepresentPositiveResult() {
    EvaluationResult result =
        new EvaluationResult(
            new FlagKey("new-checkout"),
            true,
            "flag_enabled",
            UUID.randomUUID(),
            Instant.parse("2026-05-07T10:15:30Z"));

    assertTrue(result.enabled());
    assertTrue(result.isPositive());
    assertFalse(result.isFallback());
    assertEquals("flag_enabled", result.reason());
  }

  @Test
  void shouldRepresentFallbackResult() {
    EvaluationResult result =
        new EvaluationResult(
            new FlagKey("new-checkout"),
            false,
            "fallback_disabled",
            null,
            Instant.parse("2026-05-07T10:15:30Z"));

    assertFalse(result.enabled());
    assertFalse(result.isPositive());
    assertTrue(result.isFallback());
  }
}
