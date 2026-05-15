package dev.sentinel.evaluator.model;

public enum EvaluationReason {
  FALLBACK_DISABLED,
  FLAG_ENABLED,
  GLOBAL_ENABLED,
  GLOBAL_DISABLED,
  PERCENTAGE_ROLLOUT,
  USER_TARGET,
  NO_MATCHING_RULE
}
