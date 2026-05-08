package dev.sentinel.domain.shared.exception;

/**
 * Thrown when a user lacks required permissions for an operation.
 * 
 * PermissionDeniedException is raised when an authenticated user attempts an operation
 * they do not have permission to perform (e.g., modifying a flag without EDIT permission,
 * deleting a role without ADMIN permission, etc.).
 * 
 * @author Sentinel Team
 */
public class PermissionDeniedException extends DomainException {

  /**
   * Constructs a new PermissionDeniedException with the specified message.
   * 
   * @param message the error message describing the permission denial
   */
  public PermissionDeniedException(String message) {
    super(message);
  }

  /**
   * Constructs a new PermissionDeniedException with the specified message and cause.
   * 
   * @param message the error message describing the permission denial
   * @param cause the underlying cause of this exception
   */
  public PermissionDeniedException(String message, Throwable cause) {
    super(message, cause);
  }
}
