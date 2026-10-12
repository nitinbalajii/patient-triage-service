package com.medireach.triage.dto;

import com.medireach.triage.model.DemographicGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TriageRequest {

    @NotBlank(message = "Patient identifier must not be blank")
    private String patientIdentifier;

    @NotNull(message = "Demographic group is required (PEDIATRIC, ADULT, GERIATRIC)")
    private DemographicGroup demographicGroup;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 130, message = "Age exceeds biological limit")
    private Integer age;

    @NotNull(message = "Heart rate is required")
    @Min(value = 20, message = "Heart rate must be at least 20 BPM")
    @Max(value = 260, message = "Heart rate must not exceed 260 BPM")
    private Integer heartRate;

    @NotNull(message = "Systolic blood pressure is required")
    @Min(value = 40, message = "Systolic BP must be at least 40 mmHg")
    @Max(value = 280, message = "Systolic BP must not exceed 280 mmHg")
    private Integer systolicBp;

    @NotNull(message = "Oxygen saturation percentage is required")
    @Min(value = 40, message = "SpO2 must be at least 40%")
    @Max(value = 100, message = "SpO2 cannot exceed 100%")
    private Integer oxygenSaturation;

    @NotBlank(message = "Chief complaint cannot be blank")
    private String chiefComplaint;

    public TriageRequest() {}

    public TriageRequest(String patientIdentifier, DemographicGroup demographicGroup, Integer age,
                         Integer heartRate, Integer systolicBp, Integer oxygenSaturation, String chiefComplaint) {
        this.patientIdentifier = patientIdentifier;
        this.demographicGroup = demographicGroup;
        this.age = age;
        this.heartRate = heartRate;
        this.systolicBp = systolicBp;
        this.oxygenSaturation = oxygenSaturation;
        this.chiefComplaint = chiefComplaint;
    }

    public String getPatientIdentifier() { return patientIdentifier; }
    public void setPatientIdentifier(String patientIdentifier) { this.patientIdentifier = patientIdentifier; }

    public DemographicGroup getDemographicGroup() { return demographicGroup; }
    public void setDemographicGroup(DemographicGroup demographicGroup) { this.demographicGroup = demographicGroup; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { this.heartRate = heartRate; }

    public Integer getSystolicBp() { return systolicBp; }
    public void setSystolicBp(Integer systolicBp) { this.systolicBp = systolicBp; }

    public Integer getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Integer oxygenSaturation) { this.oxygenSaturation = oxygenSaturation; }

    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
}
