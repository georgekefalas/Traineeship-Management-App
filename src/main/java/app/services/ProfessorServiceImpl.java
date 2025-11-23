package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.*;
import app.mappers.*;

import java.util.*;


@Service
public class ProfessorServiceImpl implements ProfessorService {

	
	@Autowired
	private ProfessorMapper profMapper;
	
	@Autowired
	private TraineeshipPositionMapper trainMapper;
	
	@Autowired
	private EvaluationMapper evalMapper;
	
	
	// Constructors
	
	public ProfessorServiceImpl() {
		
	}
	
	@Autowired
	public ProfessorServiceImpl(ProfessorMapper professorMapper) {
		this.profMapper = professorMapper;
	}
	
	
	// Methods
	
	@Override
	public void createProfile(Professor professor) {		
		profMapper.save(professor);
	}

	
	@Override
	public Professor retrieveProfile(String username) {
		
		Optional<Professor> prof = profMapper.findById(username);
		
		if (prof.isPresent())
			return prof.get();
		else
			return null;
	}

	
	@Override
	public List<TraineeshipPosition> showSupervisingPositions(String username) {
		
		Optional<Professor> prof = profMapper.findById(username);
		List<TraineeshipPosition> pos = null;
		
		if (prof.isPresent()) 
		{
			pos = prof.get().getPositions();
			return pos;
		}
		else
			throw new RuntimeException("Professor" + username + "not found" );
		
			
	}
	
	@Override
	public void evaluateTraineeship(Integer positionID, Evaluation evaluation) {
		
		Optional<TraineeshipPosition> pos = trainMapper.findById(positionID);
		List<Evaluation> evals = null;
				
		if (pos.isPresent())
		{
			evals = pos.get().getEvaluations();
			evals.add(evaluation);
			pos.get().setEvaluations(evals);
			evalMapper.save(evaluation);
		}
		else
			throw new RuntimeException("Traineeship not found" );
	}
	
	@Override
    public Optional<Professor> findByUsername(String username) {
        return profMapper.findByUsername(username);
    }
}



