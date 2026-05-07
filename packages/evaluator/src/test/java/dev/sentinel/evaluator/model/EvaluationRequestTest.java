package dev.sentinel.evaluator.model;

import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EvaluationRequestTest {

  @Test
  void shouldCreateRequestWithAttributes() {
    EvaluationRequest request = createRequest(Map.of("plan", "pro"));

    assertEquals("payments-service", request.applicationKey().value());
    assertEquals("prod", request.environmentKey().value());
    assertEquals("new-checkout", request.flagKey().value());
    assertEquals("user-123", request.userIdentifier().value());
    assertTrue(request.hasAttributes());
  }

  @Test
  void shouldDefaultAttributesToEmptyMap() {
    EvaluationRequest request = createRequest(null);

    assertFalse(request.hasAttributes());
    assertTrue(request.attributes().isEmpty());
  }

  @Test
  void shouldExposeImmutableAttributes() {
    EvaluationRequest request = createRequest(Map.of("plan", "pro"));

    assertThrows(
        UnsupportedOperationException.class,
        () -> request.attributes().put("region", "in"));
  }

  private static EvaluationRequest createRequest(Map<String, Object> attributes) {
    return new EvaluationRequest(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        new UserIdentifier("user-123"),
        attributes);
  }
}
