package com.certflow.api;

enum UserRole { Employee, Manager, Hr, Finance, Admin }
enum CertificationStatus { Planned, Assigned, InProgress, ExamScheduled, Passed, Failed, Expired, Cancelled }
enum ClaimType { ExamFee, TrainingCourse, LearningMaterial, RenewalFee, Other }
enum ClaimStatus { Draft, Submitted, ManagerApproved, HrApproved, FinanceApproved, ReturnedForCorrection, Rejected, Paid }
enum ApprovalDecision { Pending, Approved, Rejected, Returned }
