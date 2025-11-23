package app.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.Professor;
import app.entities.TraineeshipPosition;
import app.mappers.ProfessorMapper;
import app.mappers.TraineeshipPositionMapper;

import java.util.*;
import java.util.stream.Collectors;


@Service("interestProfessorStrategy")
public class InterestBasedProfessorStrategy implements ProfessorSelectionStrategy {

	
    @Autowired
    private ProfessorMapper profMapper;  

    @Autowired
    private TraineeshipPositionMapper posMapper; 
    
    private static final double DEFAULT_THRESHOLD = 0.5;

    private Integer positionId;  
    
    

    
    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
    

    @Override
    public List<Professor> selectProfessors() {
        
    	if (positionId == null) {
            throw new IllegalStateException("PositionId must be set before selecting professors.");
        }

        TraineeshipPosition position = posMapper.findById(positionId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid positionId: " + positionId));


        List<String> positionTopics = position.getTopics().stream()
            .map(String::toLowerCase)
            .collect(Collectors.toList());

        List<Professor> allProfessors = profMapper.findAll();

        
        return allProfessors.stream()
            .filter(professor -> {
                List<String> interests = professor.getInterests().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

                double jaccard = computeJaccardSimilarity(interests, positionTopics);
                return jaccard >= DEFAULT_THRESHOLD;
            })
            .collect(Collectors.toList());
    }
    

    private double computeJaccardSimilarity(List<String> a, List<String> b) {
        Set<String> setA = new HashSet<>(a);
        Set<String> setB = new HashSet<>(b);

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
}