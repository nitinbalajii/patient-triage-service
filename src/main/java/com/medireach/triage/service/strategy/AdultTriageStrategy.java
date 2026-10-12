package com.medireach.triage.service.strategy;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class AdultTriageStrategy implements TriageStrategy {

    @Override
    public DemographicGroup getSupportedDemographic() {
        return DemographicGroup.ADULT;
    }

    @Override
    public TriageResult evaluate(int age, int heartRate, int systolicBp, int oxygenSaturation, String chiefComplaint) {
        String complaint = chiefComplaint == null ? "" : chiefComplaint.toLowerCase(Locale.ROOT);
        int penaltyScore = 0;
        StringBuilder rationale = new StringBuilder("Adult assessment: ");

        // Red Flags - Level 1 Immediate
        if (complaint.contains("cardiac arrest") || complaint.contains("unresponsive") || complaint.contains("cyanosis")) {
            return new TriageResult(AcuityLevel.LEVEL_1_RESUSCITATION, 100, "Critical red flag: immediate resuscitation pathway required.");
        }

        // Oxygen Saturation
        if (oxygenSaturation < 90) {
            penaltyScore += 45;
            rationale.append("Severe hypoxemia (SpO2 < 90%). ");
        } else if (oxygenSaturation < 94) {
            penaltyScore += 20;
            rationale.append("Mild hypoxemia (SpO2 < 94%). ");
        }

        // Heart Rate
        if (heartRate > 130 || heartRate < 45) {
            penaltyScore += 35;
            rationale.append("Severe hemodynamic rate instability (HR: ").append(heartRate).append("). ");
        } else if (heartRate > 105 || heartRate < 55) {
            penaltyScore += 15;
            rationale.append("Borderline abnormal heart rate. ");
        }

        // Blood Pressure
        if (systolicBp < 85 || systolicBp > 200) {
            penaltyScore += 35;
            rationale.append("Severe BP crisis (Systolic: ").append(systolicBp).append("). ");
        } else if (systolicBp < 100 || systolicBp > 160) {
            penaltyScore += 15;
            rationale.append("Elevated blood pressure deviation. ");
        }

        // High risk complaints
        if (complaint.contains("chest pain") || complaint.contains("stroke") || complaint.contains("head trauma") || complaint.contains("shortness of breath")) {
            penaltyScore += 30;
            rationale.append("High-acuity chief symptom reported. ");
        }

        if (penaltyScore >= 70) {
            return new TriageResult(AcuityLevel.LEVEL_2_EMERGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 40) {
            return new TriageResult(AcuityLevel.LEVEL_3_URGENT, penaltyScore, rationale.toString().trim());
        } else if (penaltyScore >= 15) {
            return new TriageResult(AcuityLevel.LEVEL_4_LESS_URGENT, penaltyScore, rationale.toString().trim());
        } else {
            return new TriageResult(AcuityLevel.LEVEL_5_NON_URGENT, penaltyScore, "Vitals within normal baseline limits. Outpatient evaluation.");
        }
    }
}
