package dev.sentinel.evaluator.config;

import java.util.List;

/**
 * Immutable record representing configuration for a USER_TARGET rule.
 * 
 * User target rules enable flags for specific users listed in the configuration.
 * This record encapsulates the parsed list of user identifiers from a FlagRule's config JSON.
 * 
 * @param includedUserIds immutable list of user identifiers that should have the flag enabled
 * 
 * @author Mukundan Gopalachary
 */
public record UserTargetRuleConfig(List<String> includedUserIds) {
}
