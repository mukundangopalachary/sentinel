package dev.sentinel.domain.membership.model;

import dev.sentinel.domain.shared.enums.MemberStatus;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user's membership in an application with an assigned role.
 * 
 * <p>Application members link users to applications with specific roles and permissions.
 * Members can be invited (PENDING), activated (ACTIVE), or removed (REMOVED).
 * 
 * <p>Immutability: The id, applicationId, userId, invitedBy, and createdAt fields are
 * immutable. Other fields can be modified through domain methods.
 * 
 * @author Sentinel Team
 */
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

  /**
   * Constructs a new ApplicationMember.
   * 
   * @param id the unique identifier for this membership (never null)
   * @param applicationId the ID of the application (never null)
   * @param userId the ID of the user (never null)
   * @param roleId the ID of the assigned role (never null)
   * @param status the current membership status (PENDING, ACTIVE, REMOVED; never null)
   * @param invitedBy the user ID who sent the invitation (never null)
   * @param joinedAt the timestamp when the member joined the application (can be null initially)
   * @param createdAt the timestamp when the invitation was created (never null)
   * @param updatedAt the timestamp of the last update (never null)
   * @throws NullPointerException if any required parameter is null
   */
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

  /**
   * Gets the unique identifier of this membership.
   * 
   * @return the membership ID (never null)
   */
  public UUID id() {
    return id;
  }

  /**
   * Gets the ID of the application.
   * 
   * @return the application ID (never null)
   */
  public UUID applicationId() {
    return applicationId;
  }

  /**
   * Gets the ID of the user.
   * 
   * @return the user ID (never null)
   */
  public UUID userId() {
    return userId;
  }

  /**
   * Gets the ID of the assigned role.
   * 
   * @return the role ID (never null)
   */
  public UUID roleId() {
    return roleId;
  }

  /**
   * Gets the current membership status.
   * 
   * @return the status (PENDING, ACTIVE, or REMOVED)
   */
  public MemberStatus status() {
    return status;
  }

  /**
   * Gets the ID of the user who sent the invitation.
   * 
   * @return the inviter's user ID (never null)
   */
  public UUID invitedBy() {
    return invitedBy;
  }

  /**
   * Gets the timestamp when the member joined the application.
   * 
   * @return the join timestamp, or null if the invitation is still pending
   */
  public Instant joinedAt() {
    return joinedAt;
  }

  /**
   * Gets the timestamp when this membership was created.
   * 
   * @return the creation timestamp (never null)
   */
  public Instant createdAt() {
    return createdAt;
  }

  /**
   * Gets the timestamp of the last update to this membership.
   * 
   * @return the last update timestamp (never null)
   */
  public Instant updatedAt() {
    return updatedAt;
  }

  /**
   * Assigns a new role to this member.
   * 
   * @param roleId the ID of the new role (never null)
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if either parameter is null
   */
  public void assignRole(UUID roleId, Instant updatedAt) {
    this.roleId = Objects.requireNonNull(roleId, "Role id cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Activates this membership, marking the user as a full member of the application.
   * 
   * @param joinedAt the timestamp when the member joined (never null)
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if either parameter is null
   */
  public void activate(Instant joinedAt, Instant updatedAt) {
    this.status = MemberStatus.ACTIVE;
    this.joinedAt = Objects.requireNonNull(joinedAt, "Joined at cannot be null");
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Removes this member from the application.
   * 
   * @param updatedAt the timestamp of the update (never null)
   * @throws NullPointerException if updatedAt is null
   */
  public void remove(Instant updatedAt) {
    this.status = MemberStatus.REMOVED;
    this.updatedAt = requireUpdatedAt(updatedAt);
  }

  /**
   * Checks if this member is currently active.
   * 
   * @return true if the membership status is ACTIVE, false otherwise
   */
  public boolean isActive() {
    return status == MemberStatus.ACTIVE;
  }

  /**
   * Validates that updatedAt is not null.
   * 
   * @param updatedAt the timestamp to validate (must not be null)
   * @return the provided timestamp
   * @throws NullPointerException if updatedAt is null
   */
  private static Instant requireUpdatedAt(Instant updatedAt) {
    return Objects.requireNonNull(updatedAt, "Updated at cannot be null");
  }

  /**
   * Compares this membership with another object for equality.
   * 
   * <p>Two memberships are equal if they have the same ID.
   * 
   * @param obj the object to compare with
   * @return true if both objects are memberships with the same ID, false otherwise
   */
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

  /**
   * Computes a hash code for this membership based on its ID.
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
