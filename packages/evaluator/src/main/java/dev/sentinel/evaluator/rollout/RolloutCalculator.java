package dev.sentinel.evaluator.rollout;

import dev.sentinel.domain.shared.valueobject.Percentage;
import dev.sentinel.evaluator.model.EvaluationRequest;

import java.util.Objects;

/**
 * Calculator for determining user inclusion in percentage-based flag rollouts.
 * 
 * RolloutCalculator uses consistent hashing to assign users to rollout buckets (0-99).
 * The hash is deterministic and based on application, environment, flag, and user identifier,
 * ensuring the same user always gets the same result (sticky rollout).
 * 
 * @author Mukundan Gopalachary
 */
public final class RolloutCalculator {

  /**
   * Calculates the rollout bucket (0-99) for a given evaluation request.
   * 
   * Uses a hash of the application key, environment key, flag key, and user identifier
   * to deterministically assign the user to a bucket. The same request always produces
   * the same bucket, enabling consistent rollout decisions.
   * 
   * @param request the evaluation request containing context for hashing
   * @return a bucket number from 0 to 99 (inclusive)
   * @throws NullPointerException if request is null
   */
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

  /**
   * Determines if a user is within a percentage-based rollout.
   * 
   * Calculates the user's rollout bucket and checks if it is less than the percentage threshold.
   * For example, a 50% rollout enables the flag for buckets 0-49 (50 out of 100 buckets).
   * 
   * @param request the evaluation request containing user context
   * @param percentage the rollout percentage (0-100)
   * @return true if the user's bucket is within the rollout percentage, false otherwise
   * @throws NullPointerException if percentage is null
   */
  public boolean isWithinRollout(EvaluationRequest request, Percentage percentage) {
    Objects.requireNonNull(percentage, "Percentage cannot be null");
    return bucketFor(request) < percentage.value();
  }
}
