package dev.sentinel.evaluator.core;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;
import dev.sentinel.evaluator.model.FlagConfigSnapshot;
import dev.sentinel.evaluator.rule.GlobalRuleEvaluator;
import dev.sentinel.evaluator.rule.PercentageRolloutEvaluator;
import dev.sentinel.evaluator.rule.RuleEvaluator;
import dev.sentinel.evaluator.rule.UserTargetRuleEvaluator;
import dev.sentinel.evaluator.rollout.RolloutCalculator;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class FeatureFlagEvaluator {

  private final List<RuleEvaluator> ruleEvaluators;

  public FeatureFlagEvaluator() {
    this(List.of(
        new GlobalRuleEvaluator(),
        new PercentageRolloutEvaluator(new RolloutCalculator()),
        new UserTargetRuleEvaluator()));
  }

  public FeatureFlagEvaluator(List<RuleEvaluator> ruleEvaluators) {
    this.ruleEvaluators = List.copyOf(ruleEvaluators);
  }

  public EvaluationResult evaluate(EvaluationRequest request, FlagConfigSnapshot snapshot) {
    request.validateRequiredFields();

    if (!snapshot.isUsable()) {
      return buildResult(request, false, "fallback_disabled", null);
    }

    List<FlagRule> rules = snapshot.findRulesByPriority();
    if (rules.isEmpty()) {
      return buildResult(request, true, "flag_enabled", null);
    }

    for (FlagRule rule : rules) {
      if (!rule.enabled()) {
        continue;
      }

      EvaluationResult result = evaluateRule(request, rule);
      if (result != null) {
        return result;
      }
    }

    return buildResult(request, false, "no_matching_rule", null);
  }

  private EvaluationResult evaluateRule(EvaluationRequest request, FlagRule rule) {
    for (RuleEvaluator ruleEvaluator : ruleEvaluators) {
      if (ruleEvaluator.supports(rule)) {
        return ruleEvaluator.evaluate(request, rule);
      }
    }
    return null;
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
