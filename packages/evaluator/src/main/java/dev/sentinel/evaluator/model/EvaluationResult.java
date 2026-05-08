package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable result object from a feature flag evaluation.
 * 
 * EvaluationResult encapsulates the outcome of evaluating a flag: whether it is enabled or disabled,
 * the reason for that decision, and which rule (if any) matched. Results include a timestamp
 * indicating when the evaluation occurred.
 * 
 * @author Mukundan Gopalachary
 */
public final class EvaluationResult {

  private final FlagKey flagKey;
  private final boolean enabled;
  private final String reason;
  private final UUID matchedRuleId;
  private final Instant evaluatedAt;

  /**
   * Constructs a new EvaluationResult.
   * 
   * @param flagKey the evaluated feature flag identifier (not null)
   * @param enabled whether the flag is enabled for the user
   * @param reason descriptive reason for the result (e.g., "global_enabled", "user_target", "no_matching_rule") (not null)
   * @param matchedRuleId the ID of the rule that matched (null if no specific rule matched)
   * @param evaluatedAt the timestamp when evaluation occurred (not null)
   * @throws NullPointerException if flagKey, reason, or evaluatedAt is null
   */
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

  /**
   * Returns the evaluated feature flag key.
   * 
   * @return the flag identifier
   */
  public FlagKey flagKey() {
    return flagKey;
  }

  /**
   * Returns whether the flag is enabled for the user.
   * 
   * @return true if flag is enabled, false if disabled
   */
  public boolean enabled() {
    return enabled;
  }

  /**
   * Returns the reason for this evaluation result.
   * 
   * Reasons include: "global_enabled", "global_disabled", "percentage_rollout", "user_target",
   * "flag_enabled", "no_matching_rule", "fallback_disabled".
   * 
   * @return the descriptive reason string
   */
  public String reason() {
    return reason;
  }

  /**
   * Returns the ID of the matched rule.
   * 
   * @return the matched rule ID, or null if no specific rule matched
   */
  public UUID matchedRuleId() {
    return matchedRuleId;
  }

  /**
   * Returns the timestamp when this evaluation occurred.
   * 
   * @return the evaluation timestamp
   */
  public Instant evaluatedAt() {
    return evaluatedAt;
  }

  /**
   * Checks if this result indicates a positive (enabled) evaluation.
   * 
   * @return true if the flag is enabled, false otherwise
   */
  public boolean isPositive() {
    return enabled;
  }

  /**
   * Checks if this result is a fallback result.
   * 
   * Fallback results occur when the flag configuration is unusable (disabled or archived).
   * 
   * @return true if the reason is "fallback_disabled", false otherwise
   */
  public boolean isFallback() {
    return "fallback_disabled".equals(reason);
  }
}
