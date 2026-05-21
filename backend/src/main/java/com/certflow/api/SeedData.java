package com.certflow.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
class SeedData implements CommandLineRunner {
    private final UserRepository users;
    private final CertificateRepository certificates;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    SeedData(UserRepository users, CertificateRepository certificates) {
        this.users = users;
        this.certificates = certificates;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;

        var password = encoder.encode("password1");
        var managerId = UUID.randomUUID();
        users.saveAll(List.of(
            user(managerId, "manager@company.local", "Maria Manager", "Software Engineering", UserRole.Manager, null, password),
            user(UUID.randomUUID(), "employee@company.local", "Demo Employee", "Software Engineering", UserRole.Employee, managerId, password),
            user(UUID.randomUUID(), "hr@company.local", "Helen HR", "HR", UserRole.Hr, null, password),
            user(UUID.randomUUID(), "finance@company.local", "Filip Finance", "Finance", UserRole.Finance, null, password),
            user(UUID.randomUUID(), "admin@company.local", "Admin User", "IT", UserRole.Admin, null, password)
        ));

        certificates.saveAll(List.of(
            cert("Azure Fundamentals", "Microsoft", "Cloud", "Fundamentals", "AZ-900", "99", "100", 12, "https://learn.microsoft.com/certifications/azure-fundamentals/"),
            cert("Azure Developer Associate", "Microsoft", "Cloud", "Associate", "AZ-204", "165", "300", 12, null),
            cert("Solutions Architect Associate", "AWS", "Cloud", "Associate", "SAA-C03", "150", "300", 36, null),
            cert("Certified Tester Foundation Level", "ISTQB", "QA", "Foundation", "CTFL", "200", "200", null, null),
            cert("Professional Scrum Master I", "Scrum.org", "Agile", "Foundation", "PSM I", "150", "150", null, null)
        ));
    }

    private static AppUser user(UUID id, String email, String fullName, String department, UserRole role, UUID managerId, String passwordHash) {
        var user = new AppUser();
        user.id = id;
        user.email = email;
        user.fullName = fullName;
        user.department = department;
        user.role = role;
        user.managerId = managerId;
        user.passwordHash = passwordHash;
        return user;
    }

    private static Certificate cert(String name, String vendor, String category, String level, String examCode, String cost, String bonus, Integer validityMonths, String url) {
        var cert = new Certificate();
        cert.name = name;
        cert.vendor = vendor;
        cert.category = category;
        cert.level = level;
        cert.examCode = examCode;
        cert.defaultExamCost = new BigDecimal(cost);
        cert.bonusAmount = new BigDecimal(bonus);
        cert.validityMonths = validityMonths;
        cert.officialUrl = url;
        return cert;
    }
}
