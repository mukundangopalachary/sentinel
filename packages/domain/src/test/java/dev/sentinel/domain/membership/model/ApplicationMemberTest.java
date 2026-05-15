package dev.sentinel.domain.membership.model;

import dev.sentinel.domain.shared.enums.MemberStatus;
import dev.sentinel.domain.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplicationMemberTest {

  @Test
  void shouldAssignRole() {
    ApplicationMember member = createMember(MemberStatus.PENDING);
    UUID newRoleId = UUID.randomUUID();
    Instant updatedAt = Instant.parse("2026-05-08T10:15:30Z");

    member.assignRole(newRoleId, updatedAt);

    assertEquals(newRoleId, member.roleId());
    assertEquals(updatedAt, member.updatedAt());
  }

  @Test
  void shouldActivateAndRemoveMember() {
    ApplicationMember member = createMember(MemberStatus.PENDING);
    Instant joinedAt = Instant.parse("2026-05-08T10:15:30Z");
    Instant updatedAt = Instant.parse("2026-05-08T10:16:30Z");
    Instant removedAt = Instant.parse("2026-05-08T11:15:30Z");

    member.activate(joinedAt, updatedAt);
    assertEquals(MemberStatus.ACTIVE, member.status());
    assertTrue(member.isActive());
    assertEquals(joinedAt, member.joinedAt());

    member.remove(removedAt);
    assertEquals(MemberStatus.REMOVED, member.status());
    assertFalse(member.isActive());
  }

  @Test
  void shouldRejectInvalidMembershipTransitions() {
    ApplicationMember activeMember = createMember(MemberStatus.ACTIVE);
    ApplicationMember removedMember = createMember(MemberStatus.REMOVED);
    Instant updatedAt = Instant.parse("2026-05-08T10:15:30Z");

    assertThrows(InvalidStateTransitionException.class, () -> activeMember.activate(updatedAt, updatedAt));
    assertThrows(InvalidStateTransitionException.class, () -> removedMember.activate(updatedAt, updatedAt));
    assertThrows(InvalidStateTransitionException.class, () -> removedMember.remove(updatedAt));
  }

  private static ApplicationMember createMember(MemberStatus status) {
    Instant now = Instant.parse("2026-05-08T09:15:30Z");
    return new ApplicationMember(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        status,
        UUID.randomUUID(),
        null,
        now,
        now);
  }
}
