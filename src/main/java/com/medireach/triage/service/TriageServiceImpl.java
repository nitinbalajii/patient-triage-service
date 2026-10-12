package com.medireach.triage.service;

import com.medireach.triage.dto.TriageRequest;
import com.medireach.triage.dto.TriageResponse;
import com.medireach.triage.exception.PatientNotFoundException;
import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.TriageRecord;
import com.medireach.triage.repository.TriageRecordRepository;
import com.medireach.triage.service.strategy.TriageResult;
import com.medireach.triage.service.strategy.TriageStrategy;
import com.medireach.triage.service.strategy.TriageStrategyFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TriageServiceImpl implements TriageService {

    private final TriageStrategyFactory strategyFactory;
    private final TriageRecordRepository repository;

    public TriageServiceImpl(TriageStrategyFactory strategyFactory, TriageRecordRepository repository) {
        this.strategyFactory = strategyFactory;
        this.repository = repository;
    }

    @Override
    public TriageResponse assessAndRecord(TriageRequest request) {
        // Resolve appropriate strategy dynamically based on demographic group
        TriageStrategy strategy = strategyFactory.getStrategy(request.getDemographicGroup());

        // Execute clinical algorithm
        TriageResult result = strategy.evaluate(
                request.getAge(),
                request.getHeartRate(),
                request.getSystolicBp(),
                request.getOxygenSaturation(),
                request.getChiefComplaint()
        );

        // Map and persist to JPA repository
        TriageRecord record = new TriageRecord(
                request.getPatientIdentifier(),
                request.getDemographicGroup(),
                request.getAge(),
                request.getHeartRate(),
                request.getSystolicBp(),
                request.getOxygenSaturation(),
                request.getChiefComplaint(),
                result.acuityLevel(),
                result.score(),
                result.rationale(),
                result.acuityLevel().getRecommendedWard()
        );

        TriageRecord saved = repository.save(record);
        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TriageResponse> getRecentAssessments() {
        return repository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TriageResponse> getAssessmentsByAcuity(AcuityLevel level) {
        return repository.findByAcuityLevelOrderByCreatedAtDesc(level)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TriageResponse getAssessmentById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new PatientNotFoundException("Triage record with ID " + id + " not found."));
    }

    private TriageResponse toResponse(TriageRecord record) {
        return new TriageResponse(
                record.getId(),
                record.getPatientIdentifier(),
                record.getDemographicGroup(),
                record.getAcuityLevel(),
                record.getAcuityLevel().getDescription(),
                record.getCalculatedScore(),
                record.getClinicalRationale(),
                record.getAssignedWard(),
                record.getCreatedAt()
        );
    }
}
