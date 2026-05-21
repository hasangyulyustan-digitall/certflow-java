package com.certflow.api;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
class DashboardController {
    private final CurrentUserService currentUser;
    private final EmployeeCertificationRepository employeeCertifications;
    private final ClaimRepository claims;

    DashboardController(CurrentUserService currentUser, EmployeeCertificationRepository employeeCertifications, ClaimRepository claims) {
        this.currentUser = currentUser;
        this.employeeCertifications = employeeCertifications;
        this.claims = claims;
    }

    @GetMapping("/summary")
    Map<String, Object> summary() {
        var user = currentUser.get();
        var mine = employeeCertifications.findByEmployeeIdOrderByCreatedAtDesc(user.id);
        var myClaims = claims.findByEmployeeIdOrderBySubmittedAtDesc(user.id);
        var paidAmount = myClaims.stream()
            .filter(c -> c.status == ClaimStatus.Paid)
            .map(c -> c.amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
            "fullName", user.fullName,
            "role", user.role,
            "planned", mine.stream().filter(c -> c.status == CertificationStatus.Planned || c.status == CertificationStatus.InProgress).count(),
            "passed", mine.stream().filter(c -> c.status == CertificationStatus.Passed).count(),
            "pendingClaims", myClaims.stream().filter(c -> c.status != ClaimStatus.Paid && c.status != ClaimStatus.Rejected).count(),
            "paidAmount", paidAmount
        );
    }
}
