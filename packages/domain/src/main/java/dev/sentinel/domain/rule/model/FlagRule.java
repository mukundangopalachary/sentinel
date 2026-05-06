package dev.sentinel.domain.rule.model;

import dev.sentinel.domain.shared.enums.RuleType;
import dev.sentinel.domain.shared.exception.ValidationException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class FlagRule {

  private final UUID id;
  private final UUID featureFlagId;
  private final RuleType ruleType;
  private int priority;
  private boolean enabled;
  private String configJson;
  private final Instant createdAt;
  private Instant updatedAt;

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

  public UUID id() {
    return id;
  }

  public UUID featureFlagId() {
    return featureFlagId;
  }

  public RuleType ruleType() {
    return ruleType;
  }

  public int priority() {
    return priority;
  }

  public boolean enabled() {
    return enabled;
  }

  public String configJson() {
    return configJson;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant updatedAt() {
    return updatedAt;
  }

  public void changePriority(int priority, Instant updatedAt) {
    this.priority = validatePriority(priority);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void enable(Instant updatedAt) {
    this.enabled = true;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void disable(Instant updatedAt) {
    this.enabled = false;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void updateConfig(String configJson, Instant updatedAt) {
    this.configJson = normalizeConfigJson(configJson);
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  private static int validatePriority(int priority) {
    if (priority < 0) {
      throw new ValidationException("Rule priority cannot be negative");
    }

    return priority;
  }

  private static String normalizeConfigJson(String configJson) {
    Objects.requireNonNull(configJson, "Rule config cannot be null");

    String normalized = configJson.trim();
    if (normalized.isBlank()) {
      throw new ValidationException("Rule config cannot be blank");
    }

    return normalized;
  }

  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

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

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
