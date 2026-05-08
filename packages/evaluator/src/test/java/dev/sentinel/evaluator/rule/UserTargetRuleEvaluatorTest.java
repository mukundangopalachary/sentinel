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
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTargetRuleEvaluatorTest {

  @Test
  void shouldMatchIncludedUser() {
    UserTargetRuleEvaluator evaluator = new UserTargetRuleEvaluator(new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(
            createRequest("user-123"),
            createRule("{ \"includedUserIds\": [\"user-123\", \"user-456\"] }"));

    assertTrue(result.enabled());
    assertEquals("user_target", result.reason());
  }

  @Test
  void shouldReturnNullWhenUserIsNotIncluded() {
    UserTargetRuleEvaluator evaluator = new UserTargetRuleEvaluator(new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(
            createRequest("user-999"),
            createRule("{ \"includedUserIds\": [\"user-123\", \"user-456\"] }"));

    assertNull(result);
  }

  @Test
  void shouldReturnNullForInvalidConfig() {
    UserTargetRuleEvaluator evaluator = new UserTargetRuleEvaluator(new RuleConfigParser());

    EvaluationResult result =
        evaluator.evaluate(createRequest("user-123"), createRule("{ \"users\": [\"user-123\"] }"));

    assertNull(result);
  }

  private static EvaluationRequest createRequest(String userIdentifier) {
    return new EvaluationRequest(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        new UserIdentifier(userIdentifier),
        Map.of());
  }

  private static FlagRule createRule(String configJson) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        RuleType.USER_TARGET,
        1,
        true,
        configJson,
        now,
        now);
  }
}
