package com.medireach.triage.service.strategy;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class GeriatricTriageStrategy implements TriageStrategy {

    @Override
    public DemographicGroup getSupportedDemographic() {
        return DemographicGroup.GERIATRIC;
    }

    @Override
    public TriageResult evaluate(int age, int heartRate, int systolicBp, int oxygenSaturation, String chiefComplaint) {
        String complaint = chiefComplaint == null ? "" : chiefComplaint.toLowerCase(Locale.ROOT);
        int penaltyScore = 10; // Baseline fragility penalty for geriatric cohorts
        StringBuilder rationale = new StringBuilder("Geriatric protocol (Age " + age + "): ");

        if (complaint.contains("cardiac arrest") || complaint.contains("unconscious") || complaint.contains("massive bleed")) {
            return new TriageResult(AcuityLevel.LEVEL_1_RESUSCITATION, 100, "Critical geriatric collapse requiring immediate resuscitation.");
        }

        // Oxygen Saturation
        if (oxygenSaturation < 91) {
            penaltyScore += 45;
            rationale.append("Severe geriatric hypoxemia (SpO2 < 91%). ");
        } else if (oxygenSaturation < 94) {
            penaltyScore += 20;
            rationale.append("Reduced pulmonary reserve. ");
        }

        // Fragile cardiovascular vitals
        if (heartRate > 115 || heartRate < 50) {
            penaltyScore += 35;
            rationale.append("Geriatric arrhythmia risk (HR: ").append(heartRate).append("). ");
        }

        if (systolicBp < 90 || systolicBp > 190) {
            penaltyScore += 35;
            rationale.append("Severe hemodynamic instability (Systolic: ").append(systolicBp).append("). ");
        }

        // Geriatric high risk indicators: Fall, altered mental status, syncope, acute confusion
        if (complaint.contains("fall") || complaint.contains("confusion") || complaint.contains("altered mental") || complaint.contains("syncope")) {
            penaltyScore += 30;
            rationale.append("High-risk geriatric presentation (fall/delirium risk). ");
        }

        if (penaltyScore >= 70) {
            return new TriageResult(AcuityLevel.LEVEL_2_EMERGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 40) {
            return new TriageResult(AcuityLevel.LEVEL_3_URGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 20) {
            return new TriageResult(AcuityLevel.LEVEL_4_LESS_URGENT, penaltyScore, rationale.toString().trim());
        } else {
            return new TriageResult(AcuityLevel.LEVEL_5_NON_URGENT, penaltyScore, "Geriatric vitals stable. Priority routine observation.");
        }
    }
}
