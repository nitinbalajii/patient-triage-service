package com.medireach.triage.repository;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.TriageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageRecordRepository extends JpaRepository<TriageRecord, Long> {

    List<TriageRecord> findByAcuityLevelOrderByCreatedAtDesc(AcuityLevel acuityLevel);

    List<TriageRecord> findByPatientIdentifier(String patientIdentifier);

    List<TriageRecord> findTop20ByOrderByCreatedAtDesc();
}
