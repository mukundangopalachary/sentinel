package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;

import java.util.Map;
import java.util.Objects;

public final class EvaluationRequest {

  private final ApplicationKey applicationKey;
  private final EnvironmentKey environmentKey;
  private final FlagKey flagKey;
  private final UserIdentifier userIdentifier;
  private final Map<String, Object> attributes;

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

  public ApplicationKey applicationKey() {
    return applicationKey;
  }

  public EnvironmentKey environmentKey() {
    return environmentKey;
  }

  public FlagKey flagKey() {
    return flagKey;
  }

  public UserIdentifier userIdentifier() {
    return userIdentifier;
  }

  public Map<String, Object> attributes() {
    return attributes;
  }

  public boolean hasAttributes() {
    return !attributes.isEmpty();
  }

  public void validateRequiredFields() {
    if (applicationKey == null || environmentKey == null || flagKey == null || userIdentifier == null) {
      throw new ValidationException("Evaluation request is missing required fields");
    }
  }
}
