package dev.sentinel.domain.shared.valueobject;

import dev.sentinel.domain.shared.exception.ValidationException;

import java.util.regex.Pattern;
import java.util.Objects;

/**
 * Immutable value object representing an email address.
 * 
 * Email addresses are validated against a standard email pattern and automatically normalized
 * to lowercase. This value object enforces validation at construction and is immutable thereafter.
 * 
 * @author Mukundan Gopalachary
 */
public final class Email {
  
  /**
   * Regular expression pattern for email validation.
   * Matches standard RFC 5322-like email addresses (simplified).
   */
  private static final Pattern EMAIL_PATTERN =
  Pattern.compile("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$");
  
  /** The immutable email address value. */
  private final String value;
  

  /**
   * Constructs a new Email with validation.
   * 
   * The provided email is normalized to lowercase and validated against the email pattern.
   * 
   * @param email the email address string (not null, will be trimmed and lowercased)
   * @throws NullPointerException if email is null
   * @throws ValidationException if the email is blank or does not match the email pattern
   */
  public Email(String email){
    Objects.requireNonNull(email, "Email cannot be empty");
    
    String normalized = email.trim().toLowerCase();
    
    if (normalized.isBlank()) {
      throw new ValidationException("Email cannot be blank");
    }

    if(!isValid(normalized)){
      throw new ValidationException("Invalid email");
    }

    this.value = normalized;
  }
  
  /**
   * Returns the immutable email address value.
   * 
   * @return the normalized (lowercase) email address string
   */
  public String value(){
    return value;
  }
  
  /**
   * Validates the email format.
   * 
   * Checks if the email matches the email pattern.
   * 
   * @param email the email to validate
   * @return true if the email matches the pattern, false otherwise
   */
  private static boolean isValid(String email){
    return EMAIL_PATTERN.matcher(email).matches();
  }


  /**
   * Checks equality of this email with another object.
   * 
   * Two Email instances are equal if they have the same underlying value.
   * 
   * @param obj the object to compare against
   * @return true if obj is an Email with the same value, false otherwise
   */
  @Override
  public boolean equals(Object obj){
    if(this == obj) return true;
    if(!(obj instanceof Email)){
      return false;
    }
    
    Email other = (Email) obj;
    return this.value.equals(other.value);
  }

  /**
   * Returns the hash code of this email value.
   * 
   * Two Email instances have the same hash code if their underlying values are equal.
   * 
   * @return the hash code of the email value
   */
  @Override
  public int hashCode(){
    return Objects.hashCode(value);
  }

  /**
   * Returns the string representation of this email address.
   * 
   * @return the normalized email address as a string
   */
  @Override
  public String toString(){
    return value;
  }
}
