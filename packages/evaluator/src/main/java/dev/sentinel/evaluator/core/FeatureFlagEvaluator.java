package dev.sentinel.evaluator.core;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.evaluator.config.RuleConfigParser;
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

/**
 * Core feature flag evaluation engine.
 * 
 * FeatureFlagEvaluator is the main entry point for evaluating feature flags. It orchestrates
 * the evaluation of flag rules in priority order and delegates to specialized rule evaluators
 * based on rule type. The evaluator is immutable and thread-safe.
 * 
 * Evaluation process:
 * 1. Validate the evaluation request
 * 2. Check if flag configuration snapshot is valid
 * 3. Retrieve rules in priority order
 * 4. Iterate through enabled rules and find the first match
 * 5. Return evaluation result with enabled/disabled state and reason
 * 
 * @author Mukundan Gopalachary
 */
public final class FeatureFlagEvaluator {

  private final List<RuleEvaluator> ruleEvaluators;

  /**
   * Constructs a new FeatureFlagEvaluator with default rule evaluators.
   * 
   * Default evaluators support: GlobalRule, PercentageRollout, and UserTarget rules.
   */
  public FeatureFlagEvaluator() {
    this(createDefaultRuleEvaluators());
  }

  /**
   * Constructs a new FeatureFlagEvaluator with custom rule evaluators.
   * 
   * @param ruleEvaluators list of rule evaluators (will be copied to an immutable list)
   */
  public FeatureFlagEvaluator(List<RuleEvaluator> ruleEvaluators) {
    this.ruleEvaluators = List.copyOf(ruleEvaluators);
  }

  /**
   * Creates the default set of rule evaluators.
   * 
   * Instantiates evaluators for GLOBAL, PERCENTAGE_ROLLOUT, and USER_TARGET rule types.
   * 
   * @return immutable list of default rule evaluators
   */
  private static List<RuleEvaluator> createDefaultRuleEvaluators() {
    RuleConfigParser ruleConfigParser = new RuleConfigParser();
    return List.of(
        new GlobalRuleEvaluator(ruleConfigParser),
        new PercentageRolloutEvaluator(new RolloutCalculator(), ruleConfigParser),
        new UserTargetRuleEvaluator(ruleConfigParser));
  }

  /**
   * Evaluates a feature flag given an evaluation request and flag configuration snapshot.
   * 
   * Validates the request, checks snapshot usability, and evaluates rules in priority order.
   * Returns the result of the first matching enabled rule, or a fallback result if no rule matches.
   * 
   * @param request the evaluation request containing flag key, user context, and attributes
   * @param snapshot the flag configuration snapshot (rules, enabled state, etc.)
   * @return evaluation result containing enabled state, reason, and matched rule ID
   */
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

  /**
   * Evaluates a single rule by delegating to the appropriate rule evaluator.
   * 
   * Finds the first rule evaluator that supports the rule type and delegates evaluation.
   * 
   * @param request the evaluation request
   * @param rule the flag rule to evaluate
   * @return evaluation result if a matching evaluator is found, null otherwise
   */
  private EvaluationResult evaluateRule(EvaluationRequest request, FlagRule rule) {
    for (RuleEvaluator ruleEvaluator : ruleEvaluators) {
      if (ruleEvaluator.supports(rule)) {
        return ruleEvaluator.evaluate(request, rule);
      }
    }
    return null;
  }

  /**
   * Builds an evaluation result with the given parameters.
   * 
   * @param request the evaluation request
   * @param enabled whether the flag is enabled for the user
   * @param reason a descriptive reason for the evaluation result (e.g., "flag_enabled", "no_matching_rule")
   * @param matchedRuleId the ID of the rule that matched (null if no rule matched)
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
