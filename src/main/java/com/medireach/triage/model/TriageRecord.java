package com.medireach.triage.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triage_records")
public class TriageRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String patientIdentifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private DemographicGroup demographicGroup;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer heartRate;

    @Column(nullable = false)
    private Integer systolicBp;

    @Column(nullable = false)
    private Integer oxygenSaturation;

    @Column(nullable = false, length = 255)
    private String chiefComplaint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private AcuityLevel acuityLevel;

    @Column(nullable = false)
    private Integer calculatedScore;

    @Column(nullable = false, length = 500)
    private String clinicalRationale;

    @Column(nullable = false, length = 64)
    private String assignedWard;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TriageRecord() {
    }

    public TriageRecord(String patientIdentifier, DemographicGroup demographicGroup, Integer age,
                        Integer heartRate, Integer systolicBp, Integer oxygenSaturation,
                        String chiefComplaint, AcuityLevel acuityLevel, Integer calculatedScore,
                        String clinicalRationale, String assignedWard) {
        this.patientIdentifier = patientIdentifier;
        this.demographicGroup = demographicGroup;
        this.age = age;
        this.heartRate = heartRate;
        this.systolicBp = systolicBp;
        this.oxygenSaturation = oxygenSaturation;
        this.chiefComplaint = chiefComplaint;
        this.acuityLevel = acuityLevel;
        this.calculatedScore = calculatedScore;
        this.clinicalRationale = clinicalRationale;
        this.assignedWard = assignedWard;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPatientIdentifier() {
        return patientIdentifier;
    }

    public void setPatientIdentifier(String patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    public DemographicGroup getDemographicGroup() {
        return demographicGroup;
    }

    public void setDemographicGroup(DemographicGroup demographicGroup) {
        this.demographicGroup = demographicGroup;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getSystolicBp() {
        return systolicBp;
    }

    public void setSystolicBp(Integer systolicBp) {
        this.systolicBp = systolicBp;
    }

    public Integer getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(Integer oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public AcuityLevel getAcuityLevel() {
        return acuityLevel;
    }

    public void setAcuityLevel(AcuityLevel acuityLevel) {
        this.acuityLevel = acuityLevel;
    }

    public Integer getCalculatedScore() {
        return calculatedScore;
    }

    public void setCalculatedScore(Integer calculatedScore) {
        this.calculatedScore = calculatedScore;
    }

    public String getClinicalRationale() {
        return clinicalRationale;
    }

    public void setClinicalRationale(String clinicalRationale) {
        this.clinicalRationale = clinicalRationale;
    }

    public String getAssignedWard() {
        return assignedWard;
    }

    public void setAssignedWard(String assignedWard) {
        this.assignedWard = assignedWard;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
