package com.medireach.triage.service.strategy;

import com.medireach.triage.model.DemographicGroup;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TriageStrategyFactory {

    private final Map<DemographicGroup, TriageStrategy> strategyMap = new EnumMap<>(DemographicGroup.class);

    public TriageStrategyFactory(List<TriageStrategy> strategies) {
        for (TriageStrategy strategy : strategies) {
            strategyMap.put(strategy.getSupportedDemographic(), strategy);
        }
    }

    public TriageStrategy getStrategy(DemographicGroup demographicGroup) {
        TriageStrategy strategy = strategyMap.get(demographicGroup);
        if (strategy == null) {
            throw new IllegalArgumentException("No triage strategy registered for demographic: " + demographicGroup);
        }
        return strategy;
    }
}
