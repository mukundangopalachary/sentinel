package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.evaluator.config.GlobalRuleConfig;
import dev.sentinel.evaluator.config.RuleConfigParser;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

import java.time.Instant;
import java.util.UUID;

/**
 * Evaluator for GLOBAL rule type.
 * 
 * GlobalRuleEvaluator handles feature flag rules that apply universally to all users.
 * Global rules are simple on/off switches that determine if a flag is enabled for everyone.
 * 
 * @author Mukundan Gopalachary
 */
public final class GlobalRuleEvaluator implements RuleEvaluator {

  private final RuleConfigParser ruleConfigParser;

  /**
   * Constructs a new GlobalRuleEvaluator.
   * 
   * @param ruleConfigParser parser for deserializing global rule configuration
   */
  public GlobalRuleEvaluator(RuleConfigParser ruleConfigParser) {
    this.ruleConfigParser = ruleConfigParser;
  }

  /**
   * Checks if this evaluator supports GLOBAL rule type.
   * 
   * @param rule the flag rule to check
   * @return true if the rule is of type GLOBAL, false otherwise
   */
  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.GLOBAL;
  }

  /**
   * Evaluates a GLOBAL rule.
   * 
   * Parses the global rule configuration and returns the enabled/disabled state.
   * Global rules always match (either enable or disable the flag for all users).
   * 
   * @param request the evaluation request (not used for global rules)
   * @param rule the global rule to evaluate
   * @return evaluation result with the global enabled/disabled state, or null if config parsing fails
   */
  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    java.util.Optional<GlobalRuleConfig> config = ruleConfigParser.parseGlobal(rule);
    if (config.isEmpty()) {
      return null;
    }

    boolean enabled = config.get().enabled();
    String reason = enabled ? "global_enabled" : "global_disabled";
    return buildResult(request, enabled, reason, rule.id());
  }

  /**
   * Builds an evaluation result for a global rule.
   * 
   * @param request the evaluation request
   * @param enabled whether the global rule enables the flag
   * @param reason descriptive reason ("global_enabled" or "global_disabled")
   * @param matchedRuleId the ID of the matched global rule
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
