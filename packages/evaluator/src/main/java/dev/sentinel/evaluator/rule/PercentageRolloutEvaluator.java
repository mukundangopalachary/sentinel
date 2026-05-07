package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.valueobject.Percentage;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import dev.sentinel.evaluator.rollout.RolloutCalculator;

import java.time.Instant;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PercentageRolloutEvaluator implements RuleEvaluator {

  private static final Pattern PERCENTAGE_PATTERN =
      Pattern.compile("\"percentage\"\\s*:\\s*(\\d+)");

  private final RolloutCalculator rolloutCalculator;

  public PercentageRolloutEvaluator(RolloutCalculator rolloutCalculator) {
    this.rolloutCalculator = rolloutCalculator;
  }

  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.PERCENTAGE_ROLLOUT;
  }

  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    Matcher matcher = PERCENTAGE_PATTERN.matcher(rule.configJson());
    if (!matcher.find()) {
      return null;
    }

    int percentageValue = Integer.parseInt(matcher.group(1));
    boolean enabled = rolloutCalculator.isWithinRollout(request, new Percentage(percentageValue));
    if (!enabled) {
      return null;
    }

    return buildResult(request, true, "percentage_rollout", rule.id());
  }

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
