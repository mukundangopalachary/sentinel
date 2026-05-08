package dev.sentinel.evaluator.config;

/**
 * Immutable record representing configuration for a GLOBAL rule.
 * 
 * Global rules have a simple enabled/disabled state that applies universally to all users.
 * This record encapsulates the parsed configuration from a FlagRule's config JSON.
 * 
 * @param enabled whether the flag is globally enabled for all users
 * 
 * @author Mukundan Gopalachary
 */
public record GlobalRuleConfig(boolean enabled) {
}
