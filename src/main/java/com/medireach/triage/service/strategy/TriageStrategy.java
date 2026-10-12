package com.medireach.triage.service.strategy;

import com.medireach.triage.model.DemographicGroup;

public interface TriageStrategy {

    DemographicGroup getSupportedDemographic();

    TriageResult evaluate(int age, int heartRate, int systolicBp, int oxygenSaturation, String chiefComplaint);
}
