package app.services;

import java.util.*;

import app.entities.*;


public interface ProfessorService {
	
	public void createProfile(Professor professor);
	
	public Professor retrieveProfile(String username);
	
	public List<TraineeshipPosition> showSupervisingPositions(String username);
	
	public void evaluateTraineeship(Integer positionID, Evaluation evaluation);
	
	public Optional<Professor> findByUsername(String username);
	
}
