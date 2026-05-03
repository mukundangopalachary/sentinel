package dev.sentinel.domain.shared.exception;

public class InvalidRuleConfigurationException extends DomainException {

  public InvalidRuleConfigurationException(String message) {
    super(message);
  }

  public InvalidRuleConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }
}
