package dev.sentinel.domain.featureflag.model;

import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import dev.sentinel.domain.shared.exception.ValidationException;
import dev.sentinel.domain.shared.valueobject.FlagKey;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeatureFlagTest {

  @Test
  void shouldCreateFeatureFlag() {
    FeatureFlag featureFlag = createFeatureFlag(" New Checkout ", "  Controls checkout  ", true);

    assertEquals("New Checkout", featureFlag.name());
    assertEquals("Controls checkout", featureFlag.description());
    assertTrue(featureFlag.enabled());
    assertTrue(featureFlag.isEvaluable());
    assertFalse(featureFlag.isArchived());
  }

  @Test
  void shouldRenameAndChangeDescription() {
    FeatureFlag featureFlag = createFeatureFlag("Checkout", "Description", true);
    Instant renamedAt = Instant.parse("2026-05-07T10:15:30Z");
    Instant describedAt = Instant.parse("2026-05-07T11:15:30Z");

    featureFlag.rename(" New Checkout ", renamedAt);
    featureFlag.changeDescription("   ", describedAt);

    assertEquals("New Checkout", featureFlag.name());
    assertNull(featureFlag.description());
    assertEquals(describedAt, featureFlag.updatedAt());
  }

  @Test
  void shouldEnableDisableAndArchiveFeatureFlag() {
    FeatureFlag featureFlag = createFeatureFlag("Checkout", "Description", false);
    Instant enabledAt = Instant.parse("2026-05-07T10:15:30Z");
    Instant archivedAt = Instant.parse("2026-05-07T11:15:30Z");
    Instant restoredAt = Instant.parse("2026-05-07T12:15:30Z");

    featureFlag.enable(enabledAt);
    assertTrue(featureFlag.enabled());
    assertTrue(featureFlag.isEvaluable());

    featureFlag.archive(archivedAt);
    assertTrue(featureFlag.isArchived());
    assertFalse(featureFlag.isEvaluable());

    featureFlag.restore(restoredAt);
    assertFalse(featureFlag.isArchived());
    assertTrue(featureFlag.isEvaluable());
  }

  @Test
  void shouldDisableFeatureFlag() {
    FeatureFlag featureFlag = createFeatureFlag("Checkout", "Description", true);
    Instant disabledAt = Instant.parse("2026-05-07T10:15:30Z");

    featureFlag.disable(disabledAt);

    assertFalse(featureFlag.enabled());
    assertFalse(featureFlag.isEvaluable());
    assertEquals(disabledAt, featureFlag.updatedAt());
  }

  @Test
  void shouldRejectInvalidFeatureFlagTransitions() {
    FeatureFlag featureFlag = createFeatureFlag("Checkout", "Description", true);
    Instant updatedAt = Instant.parse("2026-05-07T10:15:30Z");

    assertThrows(InvalidStateTransitionException.class, () -> featureFlag.enable(updatedAt));
    featureFlag.archive(updatedAt);
    assertThrows(InvalidStateTransitionException.class, () -> featureFlag.archive(updatedAt));
    assertThrows(InvalidStateTransitionException.class, () -> featureFlag.enable(updatedAt));
  }

  @Test
  void shouldRejectBlankFeatureFlagName() {
    assertThrows(
        ValidationException.class,
        () -> createFeatureFlag("   ", "Description", true));
  }

  private static FeatureFlag createFeatureFlag(String name, String description, boolean enabled) {
    Instant now = Instant.parse("2026-05-07T09:15:30Z");
    return new FeatureFlag(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        new FlagKey("new-checkout"),
        name,
        description,
        enabled,
        UUID.randomUUID(),
        now,
        now);
  }
}
