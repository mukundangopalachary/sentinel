package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of feature flag rule types.
 * 
 * RuleType represents the different strategies for evaluating flag rules. Each rule type
 * determines how a flag's enabled state is computed for a given user or context.
 * Rules are evaluated in order and the first matching rule determines the flag state.
 * 
 * @author Sentinel Team
 */
public enum RuleType {
  /**
   * Global rule: Always applies to all users; acts as a universal on/off switch.
   */
  GLOBAL,
  /**
   * Percentage rollout: Enables flag for a percentage of users based on a hash of user ID.
   * Used for gradual feature rollout and A/B testing.
   */
  PERCENTAGE_ROLLOUT,
  /**
   * User target: Explicitly targets specific users by their identifier.
   */
  USER_TARGET,
  /**
   * User group target: Targets users in specific groups or cohorts.
   */
  USER_GROUP_TARGET,
  /**
   * Attribute match: Enables flag based on user or context attributes
   * (e.g., country == "US", plan_tier == "premium").
   */
  ATTRIBUTE_MATCH
}
