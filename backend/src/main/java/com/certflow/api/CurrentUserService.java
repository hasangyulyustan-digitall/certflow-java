package com.certflow.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
class CurrentUserService {
    private final HttpServletRequest request;
    private final UserRepository users;

    CurrentUserService(HttpServletRequest request, UserRepository users) {
        this.request = request;
        this.users = users;
    }

    AppUser get() {
        var email = request.getHeader("X-User-Email");
        if (email == null || email.isBlank()) email = "employee@company.local";
        var lookup = email.trim().toLowerCase();
        return users.findByEmail(lookup).orElseThrow(() -> new IllegalArgumentException("Unknown user: " + lookup));
    }
}
