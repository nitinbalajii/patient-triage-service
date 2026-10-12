package com.medireach.triage;

import com.medireach.triage.dto.TriageRequest;
import com.medireach.triage.dto.TriageResponse;
import com.medireach.triage.exception.PatientNotFoundException;
import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;
import com.medireach.triage.model.TriageRecord;
import com.medireach.triage.repository.TriageRecordRepository;
import com.medireach.triage.service.TriageServiceImpl;
import com.medireach.triage.service.strategy.AdultTriageStrategy;
import com.medireach.triage.service.strategy.TriageStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriageServiceTest {

    @Mock
    private TriageRecordRepository repository;

    private TriageServiceImpl triageService;

    @BeforeEach
    void setUp() {
        AdultTriageStrategy adultStrategy = new AdultTriageStrategy();
        TriageStrategyFactory factory = new TriageStrategyFactory(List.of(adultStrategy));
        triageService = new TriageServiceImpl(factory, repository);
    }

    @Test
    @DisplayName("Service: Successfully evaluates adult and saves to repository")
    void testAssessAndRecordSuccess() {
        TriageRequest req = new TriageRequest(
                "PAT-9021", DemographicGroup.ADULT, 35, 78, 122, 98, "Mild sprained wrist"
        );

        TriageRecord savedRecord = new TriageRecord(
                "PAT-9021", DemographicGroup.ADULT, 35, 78, 122, 98, "Mild sprained wrist",
                AcuityLevel.LEVEL_5_NON_URGENT, 0, "Normal limits", "Outpatient Ward"
        );

        when(repository.save(any(TriageRecord.class))).thenReturn(savedRecord);

        TriageResponse response = triageService.assessAndRecord(req);

        assertNotNull(response);
        assertEquals("PAT-9021", response.patientIdentifier());
        assertEquals(AcuityLevel.LEVEL_5_NON_URGENT, response.acuityLevel());
        verify(repository, times(1)).save(any(TriageRecord.class));
    }

    @Test
    @DisplayName("Service: Throws PatientNotFoundException for missing id")
    void testGetAssessmentByIdThrowsNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> triageService.getAssessmentById(999L));
    }
}
