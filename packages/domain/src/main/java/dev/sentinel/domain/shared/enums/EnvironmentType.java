package dev.sentinel.domain.shared.enums;

/**
 * Enumeration of environment types in the feature flag system.
 * 
 * Environments represent different deployment stages or custom environments where feature flags
 * can be configured and evaluated. Each environment type has specific characteristics and is
 * typically used for flag targeting and rollout strategies.
 * 
 * @author Mukundan Gopalachary
 */
public enum EnvironmentType {
  /**
   * Development environment: Used for local development and testing by developers.
   */
  DEV,
  /**
   * Staging environment: Production-like environment for pre-release testing and validation.
   */
  STAGING,
  /**
   * Production environment: Live environment serving actual users and customers.
   */
  PROD,
  /**
   * Custom environment: User-defined environment for specific deployment stages or use cases.
   */
  CUSTOM
}
