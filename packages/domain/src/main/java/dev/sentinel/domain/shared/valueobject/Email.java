package dev.sentinel.domain.shared.valueobject;

import java.util.regex.Pattern;
import java.util.Objects;

public final class Email {
  
  //Email Pattern Checker
  private static final Pattern EMAIL_PATTERN =
  Pattern.compile("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$");
  
  // email
  private final String value;
  

  //constructor
  public Email(String email){
    Objects.requireNonNull(email, "Email cannot be empty");
    
    String normalized = email.trim().toLowerCase();
    
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Email cannot be blank");
    }

    if(!isValid(normalized)){
      throw new IllegalArgumentException("Invalid Email");
    }

    this.value = normalized;
  }
  
  public String value(){
    return value;
  }
  
  //helper
  private static boolean isValid(String email){
    return EMAIL_PATTERN.matcher(email).matches();
  }


  //overriden methods
  @Override
  public boolean equals(Object obj){
    if(this == obj) return true;
    if(!(obj instanceof Email)){
      return false;
    }
    
    Email other = (Email) obj;
    return this.value.equals(other.value);
  }

  @Override
  public int hashCode(){
    return Objects.hashCode(value);
  }

  @Override
  public String toString(){
    return value;
  }
}
