package dev.sentinel.domain.shared.exception;

/**
 * Thrown when a rule configuration violates constraints.
 * 
 * InvalidRuleConfigurationException is raised when flag rules are configured in an invalid way
 * (e.g., invalid rule type, missing required fields, conflicting conditions, etc.).
 * 
 * @author Mukundan Gopalachary
 */
public class InvalidRuleConfigurationException extends DomainException {

  /**
   * Constructs a new InvalidRuleConfigurationException with the specified message.
   * 
   * @param message the error message describing the invalid rule configuration
   */
  public InvalidRuleConfigurationException(String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidRuleConfigurationException with the specified message and cause.
   * 
   * @param message the error message describing the invalid rule configuration
   * @param cause the underlying cause of this exception
   */
  public InvalidRuleConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
