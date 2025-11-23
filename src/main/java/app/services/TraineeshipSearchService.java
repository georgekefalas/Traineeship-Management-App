package app.services;

import app.entities.Student;
import app.entities.TraineeshipPosition;
import app.strategies.TraineeshipSearchStrategy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TraineeshipSearchService {
	

    private final Map<String, TraineeshipSearchStrategy> strategyMap;

    
    public TraineeshipSearchService(Map<String, TraineeshipSearchStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }
    

    public List<TraineeshipPosition> findMatchingPositions(String strategyKey, Student student) {
        TraineeshipSearchStrategy strategy = strategyMap.get(strategyKey);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategyKey);
        }
        return strategy.findMatchingPositions(student);
    }
}