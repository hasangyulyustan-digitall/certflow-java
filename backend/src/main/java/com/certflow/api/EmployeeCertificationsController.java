package com.certflow.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/my-certifications")
class EmployeeCertificationsController {
    private final CurrentUserService currentUser;
    private final EmployeeCertificationRepository employeeCertifications;
    private final CertificateRepository certificates;

    EmployeeCertificationsController(CurrentUserService currentUser, EmployeeCertificationRepository employeeCertifications, CertificateRepository certificates) {
        this.currentUser = currentUser;
        this.employeeCertifications = employeeCertifications;
        this.certificates = certificates;
    }

    @GetMapping
    List<EmployeeCertification> getMine() {
        var user = currentUser.get();
        return employeeCertifications.findByEmployeeIdOrderByCreatedAtDesc(user.id);
    }

    @PostMapping
    ResponseEntity<?> assign(@RequestBody AssignCertificateRequest request) {
        var user = currentUser.get();
        if (employeeCertifications.existsByEmployeeIdAndCertificateIdAndStatusNot(user.id, request.certificateId(), CertificationStatus.Cancelled)) {
            return ResponseEntity.status(409).body("This certificate is already assigned to you.");
        }

        var certificate = certificates.findById(request.certificateId()).orElse(null);
        if (certificate == null) return ResponseEntity.badRequest().body("Certificate not found.");

        var item = new EmployeeCertification();
        item.employee = user;
        item.certificate = certificate;
        item.targetDate = request.targetDate();
        item.assignedById = user.id;
        employeeCertifications.save(item);
        return ResponseEntity.ok(item);
    }
}
