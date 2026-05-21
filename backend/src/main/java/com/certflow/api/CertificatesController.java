package com.certflow.api;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/certificates")
class CertificatesController {
    private final CertificateRepository certificates;

    CertificatesController(CertificateRepository certificates) {
        this.certificates = certificates;
    }

    @GetMapping
    List<Certificate> get() {
        return certificates.findByActiveTrueOrderByVendorAscNameAsc();
    }
}
