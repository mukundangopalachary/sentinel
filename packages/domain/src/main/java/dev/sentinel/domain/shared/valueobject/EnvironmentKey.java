package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.regex.Pattern;
import java.util.Objects;

/**
 * Immutable value object representing a unique environment key.
 * 
 * Environment keys follow the pattern: lowercase alphanumeric characters with optional hyphens
 * (e.g., "production", "staging-v2"). Keys are case-insensitive and automatically normalized to
 * lowercase. This value object enforces validation at construction and is immutable thereafter.
 * 
 * @author Sentinel Team
 */
public final class EnvironmentKey {

  private static final Pattern ENVIRONMENT_KEY_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  private final String value;

  /**
   * Constructs a new EnvironmentKey with validation.
   * 
   * The provided key is normalized to lowercase and validated against the pattern:
   * lowercase alphanumeric with optional hyphens (e.g., "production", "dev-us-east").
   * 
   * @param environmentKey the environment key string (not null, will be trimmed and lowercased)
   * @throws NullPointerException if environmentKey is null
   * @throws ValidationException if the key is blank or does not match the required pattern
   */
  public EnvironmentKey(String environmentKey){
    
    Objects.requireNonNull(environmentKey, "Environment Key cannot be null");
    
    String normalized = environmentKey.trim().toLowerCase();

    if(normalized.isBlank()){
      throw new ValidationException("Environment key cannot be blank");
    }
    
    if(!isValid(normalized)){
      throw new ValidationException("Invalid environment key");
    }

    this.value = normalized;
  }

  /**
   * Validates the environment key format.
   * 
   * Checks if the key matches the pattern: lowercase alphanumeric characters with optional hyphens.
   * 
   * @param environmentKey the key to validate
   * @return true if the key matches the required pattern, false otherwise
   */
  private static boolean isValid(String environmentKey){
    return ENVIRONMENT_KEY_PATTERN.matcher(environmentKey).matches();
  }

  /**
   * Returns the immutable environment key value.
   * 
   * @return the normalized (lowercase) environment key string
   */
  public String value(){
    return value;
  }
  
  /**
   * Returns the hash code of this environment key value.
   * 
   * Two EnvironmentKey instances have the same hash code if their underlying values are equal.
   * 
   * @return the hash code of the key value
   */
  @Override
  public int hashCode(){
    return Objects.hashCode(value);
  }

  /**
   * Returns the string representation of this environment key.
   * 
   * @return the normalized key value as a string
   */
  @Override 
  public String toString(){
    return value;
  }

  /**
   * Checks equality of this environment key with another object.
   * 
   * Two EnvironmentKey instances are equal if they have the same underlying value.
   * 
   * @param obj the object to compare against
   * @return true if obj is an EnvironmentKey with the same value, false otherwise
   */
  @Override
  public boolean equals(Object obj){
    if(this == obj) return true;
    if(!(obj instanceof EnvironmentKey)) return false;

    EnvironmentKey  others = (EnvironmentKey) obj;
    return this.value.equals(others.value);
  }
}
