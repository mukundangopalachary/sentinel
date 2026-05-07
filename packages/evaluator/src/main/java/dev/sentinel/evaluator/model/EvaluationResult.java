package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class EvaluationResult {

  private final FlagKey flagKey;
  private final boolean enabled;
  private final String reason;
  private final UUID matchedRuleId;
  private final Instant evaluatedAt;

  public EvaluationResult(
      FlagKey flagKey,
      boolean enabled,
      String reason,
      UUID matchedRuleId,
      Instant evaluatedAt) {
    this.flagKey = Objects.requireNonNull(flagKey, "Flag key cannot be null");
    this.enabled = enabled;
    this.reason = Objects.requireNonNull(reason, "Reason cannot be null");
    this.matchedRuleId = matchedRuleId;
    this.evaluatedAt = Objects.requireNonNull(evaluatedAt, "Evaluated at cannot be null");
  }

  public FlagKey flagKey() {
    return flagKey;
  }

  public boolean enabled() {
    return enabled;
  }

  public String reason() {
    return reason;
  }

  public UUID matchedRuleId() {
    return matchedRuleId;
  }

  public Instant evaluatedAt() {
    return evaluatedAt;
  }

  public boolean isPositive() {
    return enabled;
  }

  public boolean isFallback() {
    return "fallback_disabled".equals(reason);
  }
}
