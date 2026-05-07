package dev.sentinel.evaluator.model;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlagConfigSnapshotTest {

  @Test
  void shouldBeUsableWhenEnabledAndNotArchived() {
    FlagConfigSnapshot snapshot = createSnapshot(true, false, List.of());

    assertTrue(snapshot.isUsable());
  }

  @Test
  void shouldNotBeUsableWhenDisabledOrArchived() {
    assertFalse(createSnapshot(false, false, List.of()).isUsable());
    assertFalse(createSnapshot(true, true, List.of()).isUsable());
  }

  @Test
  void shouldReturnRulesSortedByPriority() {
    FlagRule highPriority = createRule(1);
    FlagRule lowPriority = createRule(10);

    FlagConfigSnapshot snapshot = createSnapshot(true, false, List.of(lowPriority, highPriority));

    assertEquals(List.of(highPriority, lowPriority), snapshot.findRulesByPriority());
  }

  private static FlagConfigSnapshot createSnapshot(
      boolean flagEnabled, boolean archived, List<FlagRule> rules) {
    return new FlagConfigSnapshot(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        flagEnabled,
        archived,
        rules,
        1L,
        Instant.parse("2026-05-07T10:15:30Z"));
  }

  private static FlagRule createRule(int priority) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        RuleType.PERCENTAGE_ROLLOUT,
        priority,
        true,
        "{ \"percentage\": 25 }",
        now,
        now);
  }
}
