package com.medireach.triage.service;

import com.medireach.triage.dto.TriageRequest;
import com.medireach.triage.dto.TriageResponse;
import com.medireach.triage.model.AcuityLevel;

import java.util.List;

public interface TriageService {

    TriageResponse assessAndRecord(TriageRequest request);

    List<TriageResponse> getRecentAssessments();

    List<TriageResponse> getAssessmentsByAcuity(AcuityLevel level);

    TriageResponse getAssessmentById(Long id);
}
