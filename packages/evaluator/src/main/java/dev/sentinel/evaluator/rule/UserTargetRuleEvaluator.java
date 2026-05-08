package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.evaluator.config.RuleConfigParser;
import dev.sentinel.evaluator.config.UserTargetRuleConfig;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

import java.time.Instant;
import java.util.UUID;

/**
 * Evaluator for USER_TARGET rule type.
 * 
 * UserTargetRuleEvaluator handles feature flag rules that explicitly target specific users.
 * Evaluates whether the user in the evaluation request is in the rule's list of target users.
 * Enables the flag only for explicitly listed user IDs.
 * 
 * @author Mukundan Gopalachary
 */
public final class UserTargetRuleEvaluator implements RuleEvaluator {

  private final RuleConfigParser ruleConfigParser;

  /**
   * Constructs a new UserTargetRuleEvaluator.
   * 
   * @param ruleConfigParser parser for deserializing user target rule configuration
   */
  public UserTargetRuleEvaluator(RuleConfigParser ruleConfigParser) {
    this.ruleConfigParser = ruleConfigParser;
  }

  /**
   * Checks if this evaluator supports USER_TARGET rule type.
   * 
   * @param rule the flag rule to check
   * @return true if the rule is of type USER_TARGET, false otherwise
   */
  @Override
  public boolean supports(FlagRule rule) {
    return rule.ruleType() == RuleType.USER_TARGET;
  }

  /**
   * Evaluates a USER_TARGET rule.
   * 
   * Parses the user target configuration and checks if the request's user identifier
   * is in the list of included user IDs. Returns a result only if the user is targeted;
   * returns null if the user is not in the target list (allowing next rule to be evaluated).
   * 
   * @param request the evaluation request with user identifier
   * @param rule the user target rule to evaluate
   * @return evaluation result if user is targeted (enabled=true), null if user is not targeted or config parsing fails
   */
  @Override
  public EvaluationResult evaluate(EvaluationRequest request, FlagRule rule) {
    java.util.Optional<UserTargetRuleConfig> config = ruleConfigParser.parseUserTarget(rule);
    if (config.isEmpty()) {
      return null;
    }

    String userId = request.userIdentifier().value();
    for (String includedUserId : config.get().includedUserIds()) {
      if (userId.equals(includedUserId)) {
        return buildResult(request, true, "user_target", rule.id());
      }
    }

    return null;
  }

  /**
   * Builds an evaluation result for a user target rule.
   * 
   * @param request the evaluation request
   * @param enabled always true for user target results (enabled=true or null)
   * @param reason descriptive reason ("user_target")
   * @param matchedRuleId the ID of the matched user target rule
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
