package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;
import dev.sentinel.evaluator.config.RuleConfigParser;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import dev.sentinel.evaluator.rollout.RolloutCalculator;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PercentageRolloutEvaluatorTest {

  @Test
  void shouldMatchHundredPercentRollout() {
    PercentageRolloutEvaluator evaluator =
        new PercentageRolloutEvaluator(new RolloutCalculator(), new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createRule("{ \"percentage\": 100 }"));

    assertTrue(result.enabled());
    assertEquals("percentage_rollout", result.reason());
  }

  @Test
  void shouldReturnNullForZeroPercentRollout() {
    PercentageRolloutEvaluator evaluator =
        new PercentageRolloutEvaluator(new RolloutCalculator(), new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createRule("{ \"percentage\": 0 }"));

    assertNull(result);
  }

  @Test
  void shouldReturnNullForInvalidConfig() {
    PercentageRolloutEvaluator evaluator =
        new PercentageRolloutEvaluator(new RolloutCalculator(), new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(createRequest(), createRule("{ \"value\": 50 }"));

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
        RuleType.PERCENTAGE_ROLLOUT,
        1,
        true,
        configJson,
        now,
        now);
  }
}
