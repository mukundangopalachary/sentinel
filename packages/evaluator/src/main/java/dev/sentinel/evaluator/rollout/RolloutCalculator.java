package dev.sentinel.evaluator.rollout;

import dev.sentinel.domain.shared.valueobject.Percentage;
import dev.sentinel.evaluator.model.EvaluationRequest;

import java.util.Objects;

public final class RolloutCalculator {

  public int bucketFor(EvaluationRequest request) {
    Objects.requireNonNull(request, "Evaluation request cannot be null");

    String input = request.applicationKey().value()
        + ":"
        + request.environmentKey().value()
        + ":"
        + request.flagKey().value()
        + ":"
        + request.userIdentifier().value();

    return Math.floorMod(input.hashCode(), 100);
  }

  public boolean isWithinRollout(EvaluationRequest request, Percentage percentage) {
    Objects.requireNonNull(percentage, "Percentage cannot be null");
    return bucketFor(request) < percentage.value();
  }
}
