package dev.sentinel.evaluator.config;

import dev.sentinel.domain.shared.valueobject.Percentage;

/**
 * Immutable record representing configuration for a PERCENTAGE_ROLLOUT rule.
 * 
 * Percentage rollout rules enable flags for a percentage of users based on a hash calculation.
 * This record encapsulates the parsed configuration from a FlagRule's config JSON.
 * 
 * @param percentage the percentage of users for which the flag is enabled (0-100)
 * 
 * @author Mukundan Gopalachary
 */
public record PercentageRolloutRuleConfig(Percentage percentage) {
}
