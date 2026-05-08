package dev.sentinel.domain.shared.exception;

/**
 * Thrown when an invalid state transition is attempted.
 * 
 * InvalidStateTransitionException is raised when domain entities attempt to transition to
 * an invalid state or perform operations that are not allowed in their current state
 * (e.g., archiving an already archived flag, setting rule ordering in invalid ways, etc.).
 * 
 * @author Mukundan Gopalachary
 */
public class InvalidStateTransitionException extends DomainException {

  /**
   * Constructs a new InvalidStateTransitionException with the specified message.
   * 
   * @param message the error message describing the invalid state transition
   */
  public InvalidStateTransitionException(String message) {
    super(message);
  }

  /**
   * Constructs a new InvalidStateTransitionException with the specified message and cause.
   * 
   * @param message the error message describing the invalid state transition
   * @param cause the underlying cause of this exception
   */
  public InvalidStateTransitionException(String message, Throwable cause) {
    super(message, cause);
  }
}
