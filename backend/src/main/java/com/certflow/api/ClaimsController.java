package com.certflow.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/claims")
class ClaimsController {
    private final CurrentUserService currentUser;
    private final ClaimRepository claims;
    private final EmployeeCertificationRepository employeeCertifications;

    ClaimsController(CurrentUserService currentUser, ClaimRepository claims, EmployeeCertificationRepository employeeCertifications) {
        this.currentUser = currentUser;
        this.claims = claims;
        this.employeeCertifications = employeeCertifications;
    }

    @GetMapping("/mine")
    List<Claim> mine() {
        return claims.findByEmployeeIdOrderBySubmittedAtDesc(currentUser.get().id);
    }

    @GetMapping("/pending-approvals")
    List<Claim> pendingApprovals() {
        var user = currentUser.get();
        return claims.findByStatusNotInOrderBySubmittedAtAsc(List.of(ClaimStatus.Paid, ClaimStatus.Rejected, ClaimStatus.ReturnedForCorrection))
            .stream()
            .filter(claim -> {
                var currentStep = claim.approvalSteps.stream()
                    .sorted(Comparator.comparingInt(s -> s.stepOrder))
                    .filter(s -> s.decision == ApprovalDecision.Pending)
                    .findFirst();
                return currentStep.isPresent() && (currentStep.get().approverRole == user.role || user.role == UserRole.Admin);
            })
            .toList();
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody CreateClaimRequest request) {
        var user = currentUser.get();
        var certification = employeeCertifications.findByIdAndEmployeeId(request.employeeCertificationId(), user.id).orElse(null);
        if (certification == null) return ResponseEntity.badRequest().body("Employee certification not found for current user.");

        var claim = new Claim();
        claim.employee = user;
        claim.employeeCertification = certification;
        claim.claimType = request.claimType();
        claim.amount = request.amount();
        claim.currency = request.currency();
        claim.paymentDate = request.paymentDate();
        claim.description = request.description();
        claim.status = ClaimStatus.Submitted;
        claim.submittedAt = Instant.now();
        addStep(claim, 1, UserRole.Manager);
        addStep(claim, 2, UserRole.Hr);
        addStep(claim, 3, UserRole.Finance);
        claims.save(claim);
        return ResponseEntity.ok(claim);
    }

    @PostMapping("/{id}/approve")
    ResponseEntity<?> approve(@PathVariable UUID id, @RequestBody ApprovalRequest request) {
        var user = currentUser.get();
        var claim = claims.findDetailedById(id).orElse(null);
        if (claim == null) return ResponseEntity.notFound().build();

        var currentStep = claim.approvalSteps.stream()
            .sorted(Comparator.comparingInt(s -> s.stepOrder))
            .filter(s -> s.decision == ApprovalDecision.Pending)
            .findFirst()
            .orElse(null);
        if (currentStep == null) return ResponseEntity.badRequest().body("No pending approval step.");
        if (currentStep.approverRole != user.role && user.role != UserRole.Admin) return ResponseEntity.status(403).build();

        currentStep.decision = request.decision();
        currentStep.comment = request.comment();
        currentStep.decisionDate = Instant.now();
        currentStep.approverUserId = user.id;

        if (request.decision() == ApprovalDecision.Rejected) claim.status = ClaimStatus.Rejected;
        else if (request.decision() == ApprovalDecision.Returned) claim.status = ClaimStatus.ReturnedForCorrection;
        else if (currentStep.approverRole == UserRole.Manager) claim.status = ClaimStatus.ManagerApproved;
        else if (currentStep.approverRole == UserRole.Hr) claim.status = ClaimStatus.HrApproved;
        else if (currentStep.approverRole == UserRole.Finance) {
            claim.status = ClaimStatus.FinanceApproved;
            claim.approvedAt = Instant.now();
        }

        claims.save(claim);
        return ResponseEntity.ok(claim);
    }

    @PostMapping("/{id}/paid")
    ResponseEntity<?> paid(@PathVariable UUID id) {
        var user = currentUser.get();
        if (user.role != UserRole.Finance && user.role != UserRole.Admin) return ResponseEntity.status(403).build();
        var claim = claims.findDetailedById(id).orElse(null);
        if (claim == null) return ResponseEntity.notFound().build();
        claim.status = ClaimStatus.Paid;
        claim.paidAt = Instant.now();
        claims.save(claim);
        return ResponseEntity.ok(claim);
    }

    private static void addStep(Claim claim, int order, UserRole role) {
        var step = new ApprovalStep();
        step.claim = claim;
        step.stepOrder = order;
        step.approverRole = role;
        claim.approvalSteps.add(step);
    }
}
