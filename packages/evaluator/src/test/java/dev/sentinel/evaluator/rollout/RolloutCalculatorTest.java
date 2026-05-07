package dev.sentinel.evaluator.rollout;

import dev.sentinel.domain.shared.valueobject.ApplicationKey;
import dev.sentinel.domain.shared.valueobject.EnvironmentKey;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import dev.sentinel.domain.shared.valueobject.Percentage;
import dev.sentinel.domain.shared.valueobject.UserIdentifier;
import dev.sentinel.evaluator.model.EvaluationRequest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RolloutCalculatorTest {

  @Test
  void shouldProduceDeterministicBucketForSameRequest() {
    RolloutCalculator calculator = new RolloutCalculator();
    EvaluationRequest request = createRequest("user-123");

    int first = calculator.bucketFor(request);
    int second = calculator.bucketFor(request);

    assertEquals(first, second);
  }

  @Test
  void shouldKeepBucketWithinZeroToNinetyNine() {
    RolloutCalculator calculator = new RolloutCalculator();
    EvaluationRequest request = createRequest("user-123");

    int bucket = calculator.bucketFor(request);

    assertTrue(bucket >= 0);
    assertTrue(bucket < 100);
  }

  @Test
  void shouldAlwaysMatchHundredPercentRollout() {
    RolloutCalculator calculator = new RolloutCalculator();

    assertTrue(calculator.isWithinRollout(createRequest("user-123"), new Percentage(100)));
  }

  @Test
  void shouldNeverMatchZeroPercentRollout() {
    RolloutCalculator calculator = new RolloutCalculator();

    assertFalse(calculator.isWithinRollout(createRequest("user-123"), new Percentage(0)));
  }

  private static EvaluationRequest createRequest(String userIdentifier) {
    return new EvaluationRequest(
        new ApplicationKey("payments-service"),
        new EnvironmentKey("prod"),
        new FlagKey("new-checkout"),
        new UserIdentifier(userIdentifier),
        Map.of());
  }
}
