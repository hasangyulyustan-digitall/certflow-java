package com.certflow.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    AuthController(UserRepository users) {
        this.users = users;
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        var email = cleanEmail(request.email());
        if (email.isBlank()) return ResponseEntity.badRequest().body("Email is required.");
        if (request.fullName() == null || request.fullName().isBlank()) return ResponseEntity.badRequest().body("Full name is required.");
        if (request.password() == null || request.password().length() <= 6) return ResponseEntity.badRequest().body("Password must be longer than 6 characters.");
        if (users.existsByEmail(email)) return ResponseEntity.status(409).body("A user with this email already exists.");

        var user = new AppUser();
        user.id = UUID.randomUUID();
        user.email = email;
        user.passwordHash = encoder.encode(request.password());
        user.fullName = request.fullName().trim();
        user.department = request.department() == null ? "" : request.department().trim();
        user.role = UserRole.Employee;
        users.save(user);

        return ResponseEntity.ok(userResponse(user));
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var user = users.findByEmail(cleanEmail(request.email()));
        if (user.isEmpty() || request.password() == null || !encoder.matches(request.password(), user.get().passwordHash)) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        return ResponseEntity.ok(userResponse(user.get()));
    }

    private static String cleanEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static Map<String, Object> userResponse(AppUser user) {
        return Map.of(
            "id", user.id,
            "email", user.email,
            "fullName", user.fullName,
            "department", user.department,
            "role", user.role
        );
    }
}
