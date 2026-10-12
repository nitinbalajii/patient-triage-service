package com.medireach.triage.dto;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;

import java.time.LocalDateTime;

public record TriageResponse(
    Long recordId,
    String patientIdentifier,
    DemographicGroup demographicGroup,
    AcuityLevel acuityLevel,
    String acuityDescription,
    int calculatedScore,
    String clinicalRationale,
    String assignedWard,
    LocalDateTime assessedAt
) {}
