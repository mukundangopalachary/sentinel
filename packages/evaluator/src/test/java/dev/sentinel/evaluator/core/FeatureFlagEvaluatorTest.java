package dev.sentinel.evaluator.core;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import dev.sentinel.evaluator.model.FlagConfigSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeatureFlagEvaluatorTest {

  @Test
  void shouldReturnFallbackWhenSnapshotIsNotUsable() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();

    EvaluationResult result = evaluator.evaluate(createRequest(), createSnapshot(false, false, List.of()));

    assertFalse(result.enabled());
    assertTrue(result.isFallback());
    assertEquals("fallback_disabled", result.reason());
  }

  @Test
  void shouldReturnEnabledWhenSnapshotIsUsable() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();

    EvaluationResult result = evaluator.evaluate(createRequest(), createSnapshot(true, false, List.of()));

    assertTrue(result.enabled());
    assertFalse(result.isFallback());
    assertEquals("flag_enabled", result.reason());
    assertNull(result.matchedRuleId());
  }

  @Test
  void shouldApplyGlobalDisableRule() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();
    FlagRule globalDisable = createRule(RuleType.GLOBAL, 1, true, "{ \"enabled\": false }");

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createSnapshot(true, false, List.of(globalDisable)));

    assertFalse(result.enabled());
    assertEquals("global_disabled", result.reason());
    assertEquals(globalDisable.id(), result.matchedRuleId());
  }

  @Test
  void shouldMatchUserTargetRule() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();
    FlagRule userTarget =
        createRule(
            RuleType.USER_TARGET,
            1,
            true,
            "{ \"includedUserIds\": [\"user-123\", \"user-456\"] }");

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createSnapshot(true, false, List.of(userTarget)));

    assertTrue(result.enabled());
    assertEquals("user_target", result.reason());
    assertEquals(userTarget.id(), result.matchedRuleId());
  }

  @Test
  void shouldUsePriorityOrderAcrossRules() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();
    FlagRule laterEnable = createRule(RuleType.GLOBAL, 10, true, "{ \"enabled\": true }");
    FlagRule earlierDisable = createRule(RuleType.GLOBAL, 1, true, "{ \"enabled\": false }");

    EvaluationResult result =
        evaluator.evaluate(
            createRequest(),
            createSnapshot(true, false, List.of(laterEnable, earlierDisable)));

    assertFalse(result.enabled());
    assertEquals("global_disabled", result.reason());
    assertEquals(earlierDisable.id(), result.matchedRuleId());
  }

  @Test
  void shouldReturnNoMatchingRuleWhenNoEnabledRuleMatches() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();
    FlagRule userTarget =
        createRule(
            RuleType.USER_TARGET,
            1,
            true,
            "{ \"includedUserIds\": [\"user-999\"] }");

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createSnapshot(true, false, List.of(userTarget)));

    assertFalse(result.enabled());
    assertEquals("no_matching_rule", result.reason());
    assertNull(result.matchedRuleId());
  }

  @Test
  void shouldEvaluatePercentageRolloutRule() {
    FeatureFlagEvaluator evaluator = new FeatureFlagEvaluator();
    FlagRule percentageRule =
        createRule(RuleType.PERCENTAGE_ROLLOUT, 1, true, "{ \"percentage\": 100 }");

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createSnapshot(true, false, List.of(percentageRule)));

    assertTrue(result.enabled());
    assertEquals("percentage_rollout", result.reason());
    assertNotNull(result.matchedRuleId());
  }

  private static EvaluationRequest createRequest() {
    return new EvaluationRequest(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        new UserIdentifier("user-123"),
        Map.of());
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

  private static FlagRule createRule(
      RuleType ruleType, int priority, boolean enabled, String configJson) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        ruleType,
        priority,
        enabled,
        configJson,
        now,
        now);
  }
}
