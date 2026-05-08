package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.evaluator.config.PercentageRolloutRuleConfig;
import dev.sentinel.evaluator.config.RuleConfigParser;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import dev.sentinel.evaluator.rollout.RolloutCalculator;

import java.time.Instant;
import java.util.UUID;

/**
 * Evaluator for PERCENTAGE_ROLLOUT rule type.
 * 
 * PercentageRolloutEvaluator handles feature flag rules that enable flags for a percentage of users.
 * Uses a hash-based calculation to ensure consistent, sticky rollout (same user always gets same result).
 * Commonly used for gradual feature releases and A/B testing.
 * 
 * @author Mukundan Gopalachary
 */
public final class PercentageRolloutEvaluator implements RuleEvaluator {

  private final RolloutCalculator rolloutCalculator;
  private final RuleConfigParser ruleConfigParser;

  /**
   * Constructs a new PercentageRolloutEvaluator.
   * 
   * @param rolloutCalculator calculator for determining if a user falls within rollout percentage
   * @param ruleConfigParser parser for deserializing percentage rollout rule configuration
   */
  public PercentageRolloutEvaluator(
      RolloutCalculator rolloutCalculator, RuleConfigParser ruleConfigParser) {
    this.rolloutCalculator = rolloutCalculator;
    this.ruleConfigParser = ruleConfigParser;
  }

  /**
   * Checks if this evaluator supports PERCENTAGE_ROLLOUT rule type.
   * 
   * @param rule the flag rule to check
   * @return true if the rule is of type PERCENTAGE_ROLLOUT, false otherwise
   */
  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.PERCENTAGE_ROLLOUT;
  }

  /**
   * Evaluates a PERCENTAGE_ROLLOUT rule.
   * 
   * Parses the percentage rollout configuration and uses the rollout calculator to determine
   * if the user is included in the rollout. Returns a result only if the user is included;
   * returns null if the user is not in the rollout (allowing next rule to be evaluated).
   * 
   * @param request the evaluation request with user context
   * @param rule the percentage rollout rule to evaluate
   * @return evaluation result if user is in rollout (enabled=true), null if user is not included or config parsing fails
   */
  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    java.util.Optional<PercentageRolloutRuleConfig> config =
        ruleConfigParser.parsePercentageRollout(rule);
    if (config.isEmpty()) {
      return null;
    }

    boolean enabled =
        rolloutCalculator.isWithinRollout(request, config.get().percentage());
    if (!enabled) {
      return null;
    }

    return buildResult(request, true, "percentage_rollout", rule.id());
  }

  /**
   * Builds an evaluation result for a percentage rollout rule.
   * 
   * @param request the evaluation request
   * @param enabled always true for percentage rollout results (enabled=true or null)
   * @param reason descriptive reason ("percentage_rollout")
   * @param matchedRuleId the ID of the matched percentage rollout rule
   * @return a new EvaluationResult with the current timestamp
   */
  private EvaluationResult buildResult(
      EvaluationRequest request, boolean enabled, String reason, UUID matchedRuleId) {
    return new EvaluationResult(
        request.flagKey(),
        enabled,
        reason,
        matchedRuleId,
        Instant.now());
  }
}
