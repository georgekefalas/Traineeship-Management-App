package app.services;

import java.util.*;

import app.entities.*;


public interface CompanyService {
	
	public void  createProfile(Company company);
	
	public Company retrieveProfile(String username);
	
	public TraineeshipPosition showDetails(Integer positionID);
	
	public List<TraineeshipPosition> showAvailablePositions(String username);
	
	public List<TraineeshipPosition> showAssignedPositions(String username);
	
	public void addPosition(String username, TraineeshipPosition position);
		
	public void deletePosition (String username, Integer positionID);
	
	public void evaluateTraineeship(Integer positionID, Evaluation evaluation);
	
	public Optional<Company> findByUsername(String username);
	
}
