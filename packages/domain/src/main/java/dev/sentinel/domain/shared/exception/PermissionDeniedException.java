package dev.sentinel.domain.shared.exception;

public class PermissionDeniedException extends DomainException {

  public PermissionDeniedException(String message) {
    super(message);
  }

  public PermissionDeniedException(String message, Throwable cause) {
    super(message, cause);
  }
}
