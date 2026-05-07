package dev.sentinel.evaluator.model;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class FlagConfigSnapshot {

  private final ApplicationKey applicationKey;
  private final EnvironmentKey environmentKey;
  private final FlagKey flagKey;
  private final boolean flagEnabled;
  private final boolean archived;
  private final List<FlagRule> rules;
  private final long version;
  private final Instant loadedAt;

  public FlagConfigSnapshot(
      ApplicationKey applicationKey,
      EnvironmentKey environmentKey,
      FlagKey flagKey,
      boolean flagEnabled,
      boolean archived,
      List<FlagRule> rules,
      long version,
      Instant loadedAt) {
    this.applicationKey = Objects.requireNonNull(applicationKey, "Application key cannot be null");
    this.environmentKey = Objects.requireNonNull(environmentKey, "Environment key cannot be null");
    this.flagKey = Objects.requireNonNull(flagKey, "Flag key cannot be null");
    this.flagEnabled = flagEnabled;
    this.archived = archived;
    this.rules = rules == null ? List.of() : List.copyOf(rules);
    this.version = version;
    this.loadedAt = Objects.requireNonNull(loadedAt, "Loaded at cannot be null");
  }

  public ApplicationKey applicationKey() {
    return applicationKey;
  }

  public EnvironmentKey environmentKey() {
    return environmentKey;
  }

  public FlagKey flagKey() {
    return flagKey;
  }

  public boolean flagEnabled() {
    return flagEnabled;
  }

  public boolean archived() {
    return archived;
  }

  public List<FlagRule> rules() {
    return rules;
  }

  public long version() {
    return version;
  }

  public Instant loadedAt() {
    return loadedAt;
  }

  public boolean isUsable() {
    return flagEnabled && !archived;
  }

  public List<FlagRule> findRulesByPriority() {
    return rules.stream()
        .sorted(Comparator.comparingInt(FlagRule::priority))
        .toList();
  }
}
