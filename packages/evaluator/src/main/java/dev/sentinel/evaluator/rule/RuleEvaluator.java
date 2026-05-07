package dev.sentinel.evaluator.rule;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.evaluator.model.EvaluationRequest;
import dev.sentinel.evaluator.model.EvaluationResult;

public interface RuleEvaluator {

  boolean supports(FlagRule rule);

  EvaluationResult evaluate(EvaluationRequest request, FlagRule rule);
}
