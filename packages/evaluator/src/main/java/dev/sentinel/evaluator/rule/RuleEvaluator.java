package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

/**
 * Interface for evaluating specific types of feature flag rules.
 * 
 * RuleEvaluator defines the contract for implementations that evaluate particular rule types
 * (e.g., GlobalRule, PercentageRollout, UserTarget). Each evaluator handles parsing rule configuration
 * and determining if a flag should be enabled for a given user or context.
 * 
 * @author Mukundan Gopalachary
 */
public interface RuleEvaluator {

  /**
   * Checks if this evaluator supports the given rule type.
   * 
   * @param rule the flag rule to check
   * @return true if this evaluator can evaluate the rule, false otherwise
   */
  boolean supports(FlagRule rule);

  /**
   * Evaluates a flag rule for the given evaluation request.
   * 
   * @param request the evaluation request with user context and attributes
   * @param rule the flag rule to evaluate
   * @return an evaluation result if the rule applies (enabled/disabled), null if the rule does not match
   */
  EvaluationResult evaluate(EvaluationRequest request, FlagRule rule);
}
