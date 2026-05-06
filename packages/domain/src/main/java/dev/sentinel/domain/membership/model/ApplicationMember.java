package dev.sentinel.domain.membership.model;

import dev.sentinel.domain.shared.enums.MemberStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ApplicationMember {

  private final UUID id;
  private final UUID applicationId;
  private final UUID userId;
  private UUID roleId;
  private MemberStatus status;
  private final UUID invitedBy;
  private Instant joinedAt;
  private final Instant createdAt;
  private Instant updatedAt;

  public ApplicationMember(
      UUID id,
      UUID applicationId,
      UUID userId,
      UUID roleId,
      MemberStatus status,
      UUID invitedBy,
      Instant joinedAt,
      Instant createdAt,
      Instant updatedAt) {
    this.id = Objects.requireNonNull(id, "Application member id cannot be null");
    this.applicationId = Objects.requireNonNull(applicationId, "Application id cannot be null");
    this.userId = Objects.requireNonNull(userId, "User id cannot be null");
    this.roleId = Objects.requireNonNull(roleId, "Role id cannot be null");
    this.status = Objects.requireNonNull(status, "Member status cannot be null");
    this.invitedBy = Objects.requireNonNull(invitedBy, "Invited by cannot be null");
    this.joinedAt = joinedAt;
    this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
    this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  public UUID id() {
    return id;
  }

  public UUID applicationId() {
    return applicationId;
  }

  public UUID userId() {
    return userId;
  }

  public UUID roleId() {
    return roleId;
  }

  public MemberStatus status() {
    return status;
  }

  public UUID invitedBy() {
    return invitedBy;
  }

  public Instant joinedAt() {
    return joinedAt;
  }

  public Instant createdAt() {
    return createdAt;
  }

  public Instant updatedAt() {
    return updatedAt;
  }

  public void assignRole(UUID roleId, Instant updatedAt) {
    this.roleId = Objects.requireNonNull(roleId, "Role id cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void activate(Instant joinedAt, Instant updatedAt) {
    this.status = MemberStatus.ACTIVE;
    this.joinedAt = Objects.requireNonNull(joinedAt, "Joined at cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public void remove(Instant updatedAt) {
    this.status = MemberStatus.REMOVED;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  public boolean isActive() {
    return status == MemberStatus.ACTIVE;
  }

  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ApplicationMember)) {
      return false;
    }

    ApplicationMember other = (ApplicationMember) obj;
    return id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
