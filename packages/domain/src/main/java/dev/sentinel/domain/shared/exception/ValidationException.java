package dev.sentinel.domain.shared.exception;

/**
 * Thrown when domain validation rules are violated.
 * 
 * ValidationException is raised when an input value or operation violates domain constraints
 * (e.g., invalid email format, negative percentage, blank identifier, etc.).
 * 
 * @author Sentinel Team
 */
public class ValidationException extends DomainException {

  /**
   * Constructs a new ValidationException with the specified message.
   * 
   * @param message the error message describing the validation failure
   */
  public ValidationException(String message) {
    super(message);
  }

  /**
   * Constructs a new ValidationException with the specified message and cause.
   * 
   * @param message the error message describing the validation failure
   * @param cause the underlying cause of this exception
   */
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
