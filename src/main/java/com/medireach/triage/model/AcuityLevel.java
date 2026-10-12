package com.medireach.triage.model;

public enum AcuityLevel {
    LEVEL_1_RESUSCITATION("Immediate life-saving intervention needed", "Red Trauma Bay"),
    LEVEL_2_EMERGENT("High risk / confused / severe pain / vital sign danger", "Critical Care Area"),
    LEVEL_3_URGENT("Needs multiple resources, vital signs currently stable", "Acute Care Wing"),
    LEVEL_4_LESS_URGENT("Needs one diagnostic resource or simple procedure", "Fast Track Clinic"),
    LEVEL_5_NON_URGENT("Needs medication refill or routine clinical check", "Outpatient Ward");

    private final String description;
    private final String recommendedWard;

    AcuityLevel(String description, String recommendedWard) {
        this.description = description;
        this.recommendedWard = recommendedWard;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendedWard() {
        return recommendedWard;
    }
}
