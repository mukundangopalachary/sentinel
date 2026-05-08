package dev.sentinel.evaluator.model;

import dev.sentinel.domain.rule.model.FlagRule;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Immutable snapshot of feature flag configuration for a specific context.
 * 
 * FlagConfigSnapshot represents a read-only copy of flag configuration (enabled state, rules, etc.)
 * for a particular application, environment, and flag. Snapshots are typically loaded from a
 * cache or database and used for evaluation. They include versioning for change detection.
 * 
 * @author Mukundan Gopalachary
 */
public final class FlagConfigSnapshot {

  private final ApplicationKey applicationKey;
  private final EnvironmentKey environmentKey;
  private final FlagKey flagKey;
  private final boolean flagEnabled;
  private final boolean archived;
  private final List<FlagRule> rules;
  private final long version;
  private final Instant loadedAt;

  /**
   * Constructs a new FlagConfigSnapshot.
   * 
   * @param applicationKey the application identifier (not null)
   * @param environmentKey the environment identifier (not null)
   * @param flagKey the flag identifier (not null)
   * @param flagEnabled whether the flag is enabled
   * @param archived whether the flag is archived
   * @param rules the flag rules in this environment (null creates empty list)
   * @param version the version number for change detection
   * @param loadedAt the timestamp when this snapshot was loaded (not null)
   * @throws NullPointerException if any required parameter is null
   */
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

  /**
   * Returns the application identifier from this snapshot.
   * 
   * @return the application key
   */
  public ApplicationKey applicationKey() {
    return applicationKey;
  }

  /**
   * Returns the environment identifier from this snapshot.
   * 
   * @return the environment key
   */
  public EnvironmentKey environmentKey() {
    return environmentKey;
  }

  /**
   * Returns the flag identifier from this snapshot.
   * 
   * @return the flag key
   */
  public FlagKey flagKey() {
    return flagKey;
  }

  /**
   * Returns whether the flag is enabled.
   * 
   * @return true if flag is enabled, false otherwise
   */
  public boolean flagEnabled() {
    return flagEnabled;
  }

  /**
   * Returns whether the flag is archived.
   * 
   * Archived flags are no longer in use and should not be evaluated.
   * 
   * @return true if flag is archived, false otherwise
   */
  public boolean archived() {
    return archived;
  }

  /**
   * Returns the immutable list of flag rules in this snapshot.
   * 
   * @return the flag rules (empty list if no rules)
   */
  public List<FlagRule> rules() {
    return rules;
  }

  /**
   * Returns the version number of this snapshot.
   * 
   * Version numbers can be used to detect if a cached snapshot is stale.
   * 
   * @return the snapshot version
   */
  public long version() {
    return version;
  }

  /**
   * Returns the timestamp when this snapshot was loaded.
   * 
   * @return the loaded timestamp
   */
  public Instant loadedAt() {
    return loadedAt;
  }

  /**
   * Checks if this snapshot is usable for evaluation.
   * 
   * A snapshot is usable only if the flag is enabled and not archived.
   * 
   * @return true if the flag can be evaluated, false if it should trigger fallback behavior
   */
  public boolean isUsable() {
    return flagEnabled && !archived;
  }

  /**
   * Retrieves rules sorted by priority (ascending).
   * 
   * Rules are evaluated in priority order; lower priority values are evaluated first.
   * 
   * @return rules sorted by priority
   */
  public List<FlagRule> findRulesByPriority() {
    return rules.stream()
        .sorted(Comparator.comparingInt(FlagRule::priority))
        .toList();
  }
}
