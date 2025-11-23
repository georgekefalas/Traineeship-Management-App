package app.strategies;

import app.entities.Professor;
import app.entities.TraineeshipPosition;
import app.mappers.ProfessorMapper;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("lowestWorkloadStrategy")
public class LowestWorkloadProfessorStrategy implements ProfessorSelectionStrategy {

	
	@Autowired
    private ProfessorMapper profMapper;
	
	
	
    @Override
    public List<Professor> selectProfessors() {
    	
    	List<Professor> allProfessors = profMapper.findAll();

	    if (allProfessors == null) {
	        throw new EntityNotFoundException("No professors found in the system.");
	    }
	    
	    // Sort professors by number of assigned traineeship positions (ascending)
	    allProfessors.sort(Comparator.comparingInt(prof -> {
	        List<TraineeshipPosition> positions = prof.getPositions();
	        return positions != null ? positions.size() : 0;
	    }));
	    
	    return new ArrayList<>(allProfessors);
    }
}