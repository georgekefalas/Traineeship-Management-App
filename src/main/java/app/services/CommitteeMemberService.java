package app.services;

import app.entities.*;
import java.util.*;


public interface CommitteeMemberService {
	
	public void createProfile(CommitteeMember committeeMember);
	
	public CommitteeMember retrieveProfile(String username);
	
	public List<Student> showApplicants();
	
	public List<TraineeshipPosition> showMathingTraineeships(String username, String strategy);
	
	public void assignTraineeship(Integer positionID, String username);
	
	public List<TraineeshipPosition> showTraineeshipsInProgress();
	
	public List<Professor> showAvailableProfessors(String strategy, Integer positionId); 
	
	public void assignSupervisingProfessor(Integer positionID, String username);
	
	public List<Evaluation> monitorTraineeship(Integer positionID);
	
	public void terminateTraineeship(Integer positionID, boolean passed);
	
	public Optional<CommitteeMember> findByUsername(String username);
}
