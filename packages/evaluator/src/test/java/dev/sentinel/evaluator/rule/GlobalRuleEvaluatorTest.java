package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalRuleEvaluatorTest {

  @Test
  void shouldEnableWhenGlobalRuleConfigIsTrue() {
    GlobalRuleEvaluator evaluator = new GlobalRuleEvaluator();

    EvaluationResult result = evaluator.evaluate(createRequest(), createRule("{ \"enabled\": true }"));

    assertTrue(result.enabled());
    assertEquals("global_enabled", result.reason());
  }

  @Test
  void shouldDisableWhenGlobalRuleConfigIsFalse() {
    GlobalRuleEvaluator evaluator = new GlobalRuleEvaluator();

    EvaluationResult result = evaluator.evaluate(createRequest(), createRule("{ \"enabled\": false }"));

    assertFalse(result.enabled());
    assertEquals("global_disabled", result.reason());
  }

  @Test
  void shouldReturnNullForInvalidConfig() {
    GlobalRuleEvaluator evaluator = new GlobalRuleEvaluator();

    EvaluationResult result = evaluator.evaluate(createRequest(), createRule("{ \"value\": true }"));

    assertNull(result);
  }

  private static EvaluationRequest createRequest() {
    return new EvaluationRequest(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        new UserIdentifier("user-123"),
        Map.of());
  }

  private static FlagRule createRule(String configJson) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        RuleType.GLOBAL,
        1,
        true,
        configJson,
        now,
        now);
  }
}
