package dev.sentinel.domain.shared.valueobject;

import java.util.regex.Pattern;
import java.util.Objects;

public final class EnvironmentKey {

  private static final Pattern ENVIRONMENT_KEY_PATTERN = Pattern.compile("^[a-z0-9]+(?:-[a-z0-9]+)*$");

  private final String value;

  public EnvironmentKey(String environmentKey){
    
    Objects.requireNonNull(environmentKey, "Environment Key cannot be null");
    
    String normalized = environmentKey.trim().toLowerCase();

    if(normalized.isBlank()){
      throw new IllegalArgumentException("Environment cannot be Blank.\nExamples: DEV,STAGING,PROD,etc");
    }
    
    if(!isValid(normalized)){
      throw new IllegalArgumentException("Invalid Environment Key");
    }

    this.value = normalized;
  }

  private static boolean isValid(String environmentKey){
    return ENVIRONMENT_KEY_PATTERN.matcher(environmentKey).matches();
  }

  public String value(){
    return value;
  }
  
  @Override
  public int hashCode(){
    return Objects.hashCode(value);
  }

  @Override 
  public String toString(){
    return value;
  }

  @Override
  public boolean equals(Object obj){
    if(this == obj) return true;
    if(!(obj instanceof EnvironmentKey)) return false;

    EnvironmentKey  others = (EnvironmentKey) obj;
    return this.value.equals(others.value);
  }
}
