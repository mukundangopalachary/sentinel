package dev.sentinel.domain.rule.model;

import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.exception.ValidationException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a rule that determines how a feature flag evaluates for a given context.
 * 
 * <p>Rules define the logic for flag evaluation, including global toggles, percentage
 * rollouts, user targeting, and custom attribute matching. Rules are evaluated in
 * priority order, and the first matching rule determines the flag's value.
 * 
 * <p>Immutability: The id, featureFlagId, ruleType, and createdAt fields are immutable.
 * Other fields can be modified through domain methods.
 * 
 * @author Sentinel Team
 */
public final class FlagRule {

  private final UUID id;
  private final UUID featureFlagId;
  private final RuleType ruleType;
  private int priority;
  private boolean enabled;
  private String configJson;
  private final Instant createdAt;
  private Instant updatedAt;

  /**
   * Constructs a new FlagRule.
   * 
   * @param id the unique identifier for this rule (never null)
   * @param featureFlagId the ID of the feature flag this rule belongs to (never null)
   * @param ruleType the type of rule (e.g., GLOBAL, PERCENTAGE_ROLLOUT; never null)
   * @param priority the evaluation order; lower values are evaluated first (must be >= 0)
   * @param enabled whether this rule is currently active
   * @param configJson the JSON configuration for this rule (never blank)
   * @param createdAt the timestamp when this rule was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any non-optional parameter is null
   * @throws ValidationException if priority is negative or configJson is blank
   */
  public FlagRule(
      UUID id,
      UUID featureFlagId,
      RuleType ruleType,
      int priority,
      boolean enabled,
      String configJson,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Rule id cannot be null");
    this.featureFlagId = Objects.requireNonNull(featureFlagId, "Feature flag id cannot be null");
    this.ruleType = Objects.requireNonNull(ruleType, "Rule type cannot be null");
    this.priority = validatePriority(priority);
    this.enabled = enabled;
    this.configJson = normalizeConfigJson(configJson);
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  /**
   * Gets the unique identifier of this rule.
   * 
   * @return the rule ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the feature flag this rule belongs to.
   * 
   * @return the feature flag ID (never null)
   */
  public UUID featureFlagId() {
    return featureFlagId;
  }

  /**
   * Gets the type of this rule.
   * 
   * @return the rule type (never null)
   */
  public RuleType ruleType() {
    return ruleType;
  }

  /**
   * Gets the priority of this rule for evaluation order.
   * 
   * <p>Lower values are evaluated first. Rules are evaluated in ascending priority order.
   * 
   * @return the priority (never negative)
   */
  public int priority() {
    return priority;
  }

  /**
   * Checks if this rule is currently enabled.
   * 
   * @return true if enabled, false otherwise
   */
  public boolean enabled() {
    return enabled;
  }

  /**
   * Gets the JSON configuration for this rule.
   * 
   * @return the rule configuration as JSON (never blank)
   */
  public String configJson() {
    return configJson;
  }

  /**
   * Gets the timestamp when this rule was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this rule.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Changes the priority of this rule.
   * 
   * @param priority the new priority value (must be >= 0)
   * @param updatedAt the timestamp of the update (never null)
   * @throws ValidationException if priority is negative
   * @throws NullPointerException if updatedAt is null
   */
  public void changePriority(int priority, Instant updatedAt) {
    this.priority = validatePriority(priority);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Enables this rule.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void enable(Instant updatedAt) {
    this.enabled = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Disables this rule.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void disable(Instant updatedAt) {
    this.enabled = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Updates the configuration for this rule.
   * 
   * @param configJson the new rule configuration as JSON (must not be blank)
   * @param updatedAt the timestamp of the update (never null)
   * @throws ValidationException if configJson is blank or empty
   * @throws NullPointerException if updatedAt is null
   */
  public void updateConfig(String configJson, Instant updatedAt) {
    this.configJson = normalizeConfigJson(configJson);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Validates that a priority value is non-negative.
   * 
   * @param priority the priority to validate
   * @return the priority if valid
   * @throws ValidationException if priority is negative
   */
  private static int validatePriority(int priority) {
    if (priority < 0) {
      throw new ValidationException("Rule priority cannot be negative");
    }

    return priority;
  }

  /**
   * Validates and normalizes rule configuration JSON.
   * 
   * <p>Trims whitespace and ensures the JSON is not blank.
   * 
   * @param configJson the JSON to normalize (must not be null)
   * @return the normalized JSON
   * @throws NullPointerException if configJson is null
   * @throws ValidationException if configJson is blank after trimming
   */
  private static String normalizeConfigJson(String configJson) {
    Objects.requireNonNull(configJson, "Rule config cannot be null");

    String normalized = configJson.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Rule config cannot be blank");
    }

    return normalized;
  }

  /**
   * Validates that updatedAt is not null.
   * 
   * @param updatedAt the timestamp to validate (must not be null)
   * @return the provided timestamp
   * @throws NullPointerException if updatedAt is null
   */
  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  /**
   * Compares this rule with another object for equality.
   * 
   * <p>Two rules are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are rules with the same ID, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof FlagRule)) {
      return false;
    }

    FlagRule other = (FlagRule) obj;
    return id.equals(other.id);
  }

  /**
   * Computes a hash code for this rule based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
