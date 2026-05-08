package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;

import java.util.Map;
import java.util.Objects;

/**
 * Immutable request object for feature flag evaluation.
 * 
 * EvaluationRequest encapsulates all context needed to evaluate a feature flag, including
 * the application, environment, flag identifier, and user context. Attributes are optional
 * and can be used for advanced rule evaluation (e.g., attribute matching rules).
 * 
 * @author Mukundan Gopalachary
 */
public final class EvaluationRequest {

  private final ApplicationKey applicationKey;
  private final EnvironmentKey environmentKey;
  private final FlagKey flagKey;
  private final UserIdentifier userIdentifier;
  private final Map<String, Object> attributes;

  /**
   * Constructs a new EvaluationRequest.
   * 
   * All keys must be non-null; attributes may be null (treated as empty map).
   * 
   * @param applicationKey the application identifier (not null)
   * @param environmentKey the environment identifier (not null)
   * @param flagKey the feature flag identifier (not null)
   * @param userIdentifier the end-user identifier (not null)
   * @param attributes optional user/context attributes for rule evaluation (null creates empty map)
   * @throws NullPointerException if any key is null
   */
  public EvaluationRequest(
      ApplicationKey applicationKey,
      EnvironmentKey environmentKey,
      FlagKey flagKey,
      UserIdentifier userIdentifier,
      Map<String, Object> attributes) {
    this.applicationKey = Objects.requireNonNull(applicationKey, "Application key cannot be null");
    this.environmentKey = Objects.requireNonNull(environmentKey, "Environment key cannot be null");
    this.flagKey = Objects.requireNonNull(flagKey, "Flag key cannot be null");
    this.userIdentifier = Objects.requireNonNull(userIdentifier, "User identifier cannot be null");
    this.attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
  }

  /**
   * Returns the application key from this request.
   * 
   * @return the application identifier
   */
  public ApplicationKey applicationKey() {
    return applicationKey;
  }

  /**
   * Returns the environment key from this request.
   * 
   * @return the environment identifier
   */
  public EnvironmentKey environmentKey() {
    return environmentKey;
  }

  /**
   * Returns the flag key from this request.
   * 
   * @return the feature flag identifier
   */
  public FlagKey flagKey() {
    return flagKey;
  }

  /**
   * Returns the user identifier from this request.
   * 
   * @return the end-user identifier for the evaluation
   */
  public UserIdentifier userIdentifier() {
    return userIdentifier;
  }

  /**
   * Returns the immutable attributes map from this request.
   * 
   * @return the user/context attributes (empty map if no attributes provided)
   */
  public Map<String, Object> attributes() {
    return attributes;
  }

  /**
   * Checks if this request has any attributes.
   * 
   * @return true if attributes map is non-empty, false otherwise
   */
  public boolean hasAttributes() {
    return !attributes.isEmpty();
  }

  /**
   * Validates that all required fields are present (non-null).
   * 
   * @throws ValidationException if any required field is null
   */
  public void validateRequiredFields() {
    if (applicationKey == null || environmentKey == null || flagKey == null || userIdentifier == null) {
      throw new ValidationException("Evaluation request is missing required fields");
    }
  }
}
