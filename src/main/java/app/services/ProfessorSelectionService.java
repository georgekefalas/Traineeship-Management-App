package app.services;

import app.entities.Professor;
import app.strategies.InterestBasedProfessorStrategy;
import app.strategies.ProfessorSelectionStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProfessorSelectionService {

    private final Map<String, ProfessorSelectionStrategy> strategyMap;

    public ProfessorSelectionService(Map<String, ProfessorSelectionStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public List<Professor> selectProfessors(String strategyKey, Integer positionId) {
        
    	ProfessorSelectionStrategy strategy = strategyMap.get(strategyKey);
        
    	if (strategy == null) {
            throw new IllegalArgumentException("Unknown professor selection strategy: " + strategyKey);
        }
    	
    	if (strategy instanceof InterestBasedProfessorStrategy interestStrategy) {
            interestStrategy.setPositionId(positionId);
        }
        
    	return strategy.selectProfessors();
    }
}
