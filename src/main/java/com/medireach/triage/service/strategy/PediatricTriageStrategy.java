package com.medireach.triage.service.strategy;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class PediatricTriageStrategy implements TriageStrategy {

    @Override
    public DemographicGroup getSupportedDemographic() {
        return DemographicGroup.PEDIATRIC;
    }

    @Override
    public TriageResult evaluate(int age, int heartRate, int systolicBp, int oxygenSaturation, String chiefComplaint) {
        String complaint = chiefComplaint == null ? "" : chiefComplaint.toLowerCase(Locale.ROOT);
        int penaltyScore = 0;
        StringBuilder rationale = new StringBuilder("Pediatric protocol (Age " + age + "): ");

        if (complaint.contains("apnea") || complaint.contains("unresponsive") || complaint.contains("status epilepticus")) {
            return new TriageResult(AcuityLevel.LEVEL_1_RESUSCITATION, 100, "Critical pediatric emergency requiring immediate resuscitation.");
        }

        // Pediatric SpO2 threshold is stricter
        if (oxygenSaturation < 92) {
            penaltyScore += 50;
            rationale.append("Severe pediatric hypoxia (SpO2 < 92%). ");
        } else if (oxygenSaturation < 95) {
            penaltyScore += 25;
            rationale.append("Desaturation warning in pediatric patient. ");
        }

        // Pediatric Heart Rate adjustments
        if (heartRate > 170 || heartRate < 70) {
            penaltyScore += 40;
            rationale.append("Critical pediatric heart rate derangement (HR: ").append(heartRate).append("). ");
        } else if (heartRate > 140) {
            penaltyScore += 20;
            rationale.append("Tachycardia noted. ");
        }

        // High risk pediatric symptoms
        if (complaint.contains("stridor") || complaint.contains("febrile seizure") || complaint.contains("lethargic") || complaint.contains("dehydration")) {
            penaltyScore += 30;
            rationale.append("Pediatric high-alert symptom detected. ");
        }

        if (penaltyScore >= 65) {
            return new TriageResult(AcuityLevel.LEVEL_2_EMERGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 35) {
            return new TriageResult(AcuityLevel.LEVEL_3_URGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 15) {
            return new TriageResult(AcuityLevel.LEVEL_4_LESS_URGENT, penaltyScore, rationale.toString().trim());
        } else {
            return new TriageResult(AcuityLevel.LEVEL_5_NON_URGENT, penaltyScore, "Stable pediatric vitals. Routine pediatric clinic queue.");
        }
    }
}
