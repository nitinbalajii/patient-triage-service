package com.medireach.triage.service.strategy;

import com.medireach.triage.model.AcuityLevel;

public record TriageResult(
    AcuityLevel acuityLevel,
    int score,
    String rationale
) {}
