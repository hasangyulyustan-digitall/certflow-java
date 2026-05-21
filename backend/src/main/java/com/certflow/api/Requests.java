package com.certflow.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

record RegisterRequest(String email, String password, String fullName, String department) {}
record LoginRequest(String email, String password) {}
record AssignCertificateRequest(UUID certificateId, LocalDate targetDate) {}
record CreateClaimRequest(UUID employeeCertificationId, ClaimType claimType, BigDecimal amount, String currency, LocalDate paymentDate, String description) {}
record ApprovalRequest(ApprovalDecision decision, String comment) {}
