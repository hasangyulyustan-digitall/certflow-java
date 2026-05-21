package com.certflow.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Table(name = "app_users", indexes = @Index(name = "ix_app_users_email", columnList = "email", unique = true))
class AppUser {
    @Id UUID id = UUID.randomUUID();
    @Column(nullable = false, unique = true) String email = "";
    @JsonIgnore @Column(nullable = false) String passwordHash = "";
    @Column(nullable = false) String fullName = "";
    @Column(nullable = false) String department = "";
    @Enumerated(EnumType.STRING) @Column(nullable = false) UserRole role = UserRole.Employee;
    UUID managerId;
}

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Certificate {
    @Id UUID id = UUID.randomUUID();
    @Column(nullable = false) String name = "";
    @Column(nullable = false) String vendor = "";
    @Column(nullable = false) String category = "";
    @Column(nullable = false) String level = "";
    String examCode;
    String description;
    String officialUrl;
    @Column(nullable = false, precision = 12, scale = 2) BigDecimal defaultExamCost = BigDecimal.ZERO;
    @Column(nullable = false) String currency = "EUR";
    @Column(nullable = false, precision = 12, scale = 2) BigDecimal bonusAmount = BigDecimal.ZERO;
    Integer validityMonths;
    boolean requiresManagerApproval = true;
    boolean active = true;

    public boolean isIsActive() {
        return active;
    }
}

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class EmployeeCertification {
    @Id UUID id = UUID.randomUUID();
    @ManyToOne(optional = false) AppUser employee;
    @ManyToOne(optional = false) Certificate certificate;
    @Enumerated(EnumType.STRING) @Column(nullable = false) CertificationStatus status = CertificationStatus.Planned;
    LocalDate targetDate;
    LocalDate completedAt;
    LocalDate expiryDate;
    UUID assignedById;
    Instant createdAt = Instant.now();
}

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Claim {
    @Id UUID id = UUID.randomUUID();
    @ManyToOne(optional = false) AppUser employee;
    @ManyToOne(optional = false) EmployeeCertification employeeCertification;
    @Enumerated(EnumType.STRING) @Column(nullable = false) ClaimType claimType;
    @Column(nullable = false, precision = 12, scale = 2) BigDecimal amount;
    @Column(nullable = false) String currency = "EUR";
    @Column(nullable = false) LocalDate paymentDate;
    String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false) ClaimStatus status = ClaimStatus.Draft;
    Instant submittedAt;
    Instant approvedAt;
    Instant paidAt;
    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true) List<ApprovalStep> approvalSteps = new ArrayList<>();
}

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class ApprovalStep {
    @Id UUID id = UUID.randomUUID();
    @JsonIgnore @ManyToOne(optional = false) Claim claim;
    int stepOrder;
    @Enumerated(EnumType.STRING) @Column(nullable = false) UserRole approverRole;
    UUID approverUserId;
    @Enumerated(EnumType.STRING) @Column(nullable = false) ApprovalDecision decision = ApprovalDecision.Pending;
    Instant decisionDate;
    String comment;
}
