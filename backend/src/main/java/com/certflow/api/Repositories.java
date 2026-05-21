package com.certflow.api;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface UserRepository extends JpaRepository<AppUser, UUID> {
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
}

interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    List<Certificate> findByActiveTrueOrderByVendorAscNameAsc();
}

interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertification, UUID> {
    @EntityGraph(attributePaths = "certificate")
    List<EmployeeCertification> findByEmployeeIdOrderByCreatedAtDesc(UUID employeeId);
    boolean existsByEmployeeIdAndCertificateIdAndStatusNot(UUID employeeId, UUID certificateId, CertificationStatus status);
    Optional<EmployeeCertification> findByIdAndEmployeeId(UUID id, UUID employeeId);
}

interface ClaimRepository extends JpaRepository<Claim, UUID> {
    @EntityGraph(attributePaths = {"employeeCertification", "employeeCertification.certificate", "approvalSteps"})
    List<Claim> findByEmployeeIdOrderBySubmittedAtDesc(UUID employeeId);

    @EntityGraph(attributePaths = {"employeeCertification", "employeeCertification.certificate", "approvalSteps"})
    Optional<Claim> findDetailedById(UUID id);

    @EntityGraph(attributePaths = {"employee", "employeeCertification", "employeeCertification.certificate", "approvalSteps"})
    List<Claim> findByStatusNotInOrderBySubmittedAtAsc(List<ClaimStatus> statuses);
}
