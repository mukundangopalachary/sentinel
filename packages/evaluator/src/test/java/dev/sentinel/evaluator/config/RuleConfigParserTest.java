package dev.sentinel.evaluator.config;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleConfigParserTest {

  private final RuleConfigParser parser = new RuleConfigParser();

  @Test
  void shouldParseGlobalRuleConfig() {
    FlagRule rule = createRule(RuleType.GLOBAL, "{ \"enabled\": true }");

    var config = parser.parseGlobal(rule);

    assertTrue(config.isPresent());
    assertTrue(config.get().enabled());
  }

  @Test
  void shouldParsePercentageRuleConfig() {
    FlagRule rule = createRule(RuleType.PERCENTAGE_ROLLOUT, "{ \"percentage\": 25 }");

    var config = parser.parsePercentageRollout(rule);

    assertTrue(config.isPresent());
    assertEquals(25, config.get().percentage().value());
  }

  @Test
  void shouldParseUserTargetRuleConfig() {
    FlagRule rule =
        createRule(RuleType.USER_TARGET, "{ \"includedUserIds\": [\"user-123\", \"user-456\"] }");

    var config = parser.parseUserTarget(rule);

    assertTrue(config.isPresent());
    assertEquals(2, config.get().includedUserIds().size());
    assertEquals("user-123", config.get().includedUserIds().get(0));
  }

  @Test
  void shouldReturnEmptyWhenConfigDoesNotMatchExpectedShape() {
    FlagRule rule = createRule(RuleType.GLOBAL, "{ \"value\": true }");

    assertFalse(parser.parseGlobal(rule).isPresent());
  }

  private static FlagRule createRule(RuleType ruleType, String configJson) {
    Instant now = Instant.parse("2026-05-08T09:15:30Z");
    return new FlagRule(
        UUID.randomUUID(),
        UUID.randomUUID(),
        ruleType,
        1,
        true,
        configJson,
        now,
        now);
  }
}
