package com.medireach.triage.controller;

import com.medireach.triage.dto.TriageRequest;
import com.medireach.triage.dto.TriageResponse;
import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/triage")
@CrossOrigin(origins = "*")
public class TriageController {

    private final TriageService triageService;

    public TriageController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping("/assess")
    public ResponseEntity<TriageResponse> assessPatient(@Valid @RequestBody TriageRequest request) {
        TriageResponse response = triageService.assessAndRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TriageResponse>> getRecentAssessments() {
        return ResponseEntity.ok(triageService.getRecentAssessments());
    }

    @GetMapping("/acuity/{level}")
    public ResponseEntity<List<TriageResponse>> getByAcuity(@PathVariable AcuityLevel level) {
        return ResponseEntity.ok(triageService.getAssessmentsByAcuity(level));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TriageResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(triageService.getAssessmentById(id));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "patient-triage-service",
                "engine", "Spring Boot 3 + Strategy Pattern"
        ));
    }
}
