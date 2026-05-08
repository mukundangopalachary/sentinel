package dev.sentinel.domain.shared.exception;

/**
 * Base exception for all domain-level errors in the Sentinel feature flag system.
 * 
 * DomainException is an unchecked exception (RuntimeException) that serves as the root
 * exception for all domain-specific violations and business rule failures. Subclasses
 * represent specific domain error scenarios (validation errors, state violations, permission
 * denials, etc.).
 * 
 * @author Mukundan Gopalachary
 */
public class DomainException extends RuntimeException {

  /**
   * Constructs a new DomainException with the specified message.
   * 
   * @param message the error message describing the domain violation
   */
  public DomainException(String message) {
    super(message);
  }

  /**
   * Constructs a new DomainException with the specified message and cause.
   * 
   * @param message the error message describing the domain violation
   * @param cause the underlying cause of this exception (e.g., a database error)
   */
  public DomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
