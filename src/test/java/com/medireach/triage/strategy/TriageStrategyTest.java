package com.medireach.triage.strategy;

import com.medireach.triage.model.AcuityLevel;
import com.medireach.triage.model.DemographicGroup;
import com.medireach.triage.service.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriageStrategyTest {

    private AdultTriageStrategy adultStrategy;
    private PediatricTriageStrategy pediatricStrategy;
    private GeriatricTriageStrategy geriatricStrategy;
    private TriageStrategyFactory factory;

    @BeforeEach
    void setUp() {
        adultStrategy = new AdultTriageStrategy();
        pediatricStrategy = new PediatricTriageStrategy();
        geriatricStrategy = new GeriatricTriageStrategy();
        factory = new TriageStrategyFactory(List.of(adultStrategy, pediatricStrategy, geriatricStrategy));
    }

    @Test
    @DisplayName("Adult: Critical symptoms trigger Level 1 Resuscitation immediately")
    void testAdultCardiacArrestTriggersResuscitation() {
        TriageResult result = adultStrategy.evaluate(45, 0, 0, 70, "Suspected cardiac arrest");
        assertEquals(AcuityLevel.LEVEL_1_RESUSCITATION, result.acuityLevel());
        assertEquals(100, result.score());
    }

    @Test
    @DisplayName("Adult: Normal vitals with mild complaint triggers Level 5 Non-Urgent")
    void testAdultNormalVitalsTriggersNonUrgent() {
        TriageResult result = adultStrategy.evaluate(30, 72, 120, 99, "Routine prescription renewal");
        assertEquals(AcuityLevel.LEVEL_5_NON_URGENT, result.acuityLevel());
        assertTrue(result.score() < 15);
    }

    @Test
    @DisplayName("Pediatric: Hypoxia under 92% triggers Level 2 Emergent")
    void testPediatricHypoxiaTriggersEmergent() {
        TriageResult result = pediatricStrategy.evaluate(4, 130, 95, 88, "Persistent wheezing");
        assertEquals(AcuityLevel.LEVEL_2_EMERGENT, result.acuityLevel());
        assertTrue(result.score() >= 65);
    }

    @Test
    @DisplayName("Geriatric: Fall with altered mental status triggers acute escalation")
    void testGeriatricFallWithConfusion() {
        TriageResult result = geriatricStrategy.evaluate(78, 92, 135, 96, "Patient had sudden fall with confusion");
        assertTrue(result.score() >= 40);
        assertTrue(result.acuityLevel() == AcuityLevel.LEVEL_3_URGENT || result.acuityLevel() == AcuityLevel.LEVEL_2_EMERGENT);
    }

    @Test
    @DisplayName("Factory: Resolves registered demographic strategies accurately")
    void testFactoryResolvesStrategies() {
        assertSame(adultStrategy, factory.getStrategy(DemographicGroup.ADULT));
        assertSame(pediatricStrategy, factory.getStrategy(DemographicGroup.PEDIATRIC));
        assertSame(geriatricStrategy, factory.getStrategy(DemographicGroup.GERIATRIC));
    }
}
