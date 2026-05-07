package dev.sentinel.domain.rule.model;

import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlagRuleTest {

  @Test
  void shouldCreateFlagRule() {
    FlagRule flagRule = createRule(10, true, "{ \"percentage\": 25 }");

    assertEquals(RuleType.PERCENTAGE_ROLLOUT, flagRule.ruleType());
    assertEquals(10, flagRule.priority());
    assertTrue(flagRule.enabled());
    assertEquals("{ \"percentage\": 25 }", flagRule.configJson());
  }

  @Test
  void shouldChangePriority() {
    FlagRule flagRule = createRule(10, true, "{ \"percentage\": 25 }");
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    flagRule.changePriority(5, updatedAt);

    assertEquals(5, flagRule.priority());
    assertEquals(updatedAt, flagRule.updatedAt());
  }

  @Test
  void shouldEnableDisableAndUpdateConfig() {
    FlagRule flagRule = createRule(10, false, "{ \"percentage\": 25 }");
    Instant enabledAt = Instant.parse("2026-05-07T10:15:30Z");
    Instant configUpdatedAt = Instant.parse("2026-05-07T11:15:30Z");
    Instant disabledAt = Instant.parse("2026-05-07T12:15:30Z");

    flagRule.enable(enabledAt);
    assertTrue(flagRule.enabled());

    flagRule.updateConfig("  { \"percentage\": 50 }  ", configUpdatedAt);
    assertEquals("{ \"percentage\": 50 }", flagRule.configJson());
    assertEquals(configUpdatedAt, flagRule.updatedAt());

    flagRule.disable(disabledAt);
    assertFalse(flagRule.enabled());
    assertEquals(disabledAt, flagRule.updatedAt());
  }

  @Test
  void shouldRejectNegativePriority() {
    assertThrows(
        ValidationException.class,
        () -> createRule(-1, true, "{ \"percentage\": 25 }"));
  }

  @Test
  void shouldRejectBlankConfigJson() {
    assertThrows(
        ValidationException.class,
        () -> createRule(10, true, "   "));
  }

  private static FlagRule createRule(int priority, boolean enabled, String configJson) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        RuleType.PERCENTAGE_ROLLOUT,
        priority,
        enabled,
        configJson,
        now,
        now);
  }
}
