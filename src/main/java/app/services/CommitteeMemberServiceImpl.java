package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import app.mappers.*;
import app.entities.*;

import java.util.*;

@Service
public class CommitteeMemberServiceImpl implements CommitteeMemberService {
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private ProfessorMapper profMapper;
	
	@Autowired
	private TraineeshipPositionMapper trainMapper;
	
	@Autowired
	private CommitteeMemberMapper comMapper;
	
	@Autowired
    private TraineeshipSearchService traineeshipSearchService;
	
	@Autowired
    private ProfessorSelectionService professorSelectionService;
	
	
	// Constructors

	public CommitteeMemberServiceImpl() {
			
	}
		
	@Autowired
	public CommitteeMemberServiceImpl(CommitteeMemberMapper committeeMemberMapper) {
		this.comMapper = committeeMemberMapper;
	}
	
	
	// Methods

	@Override
	public void createProfile(CommitteeMember committeeMember) {
		comMapper.save(committeeMember);
		
	}
	

	@Override
	public CommitteeMember retrieveProfile(String username) {
		Optional<CommitteeMember> committeeMember = comMapper.findById(username);
		
		if (committeeMember.isPresent())
			return committeeMember.get();
		else
			return null;
	}
	

	@Override
	public List<Student> showApplicants() {
		List<Student> allStudents = studentMapper.findAll();
		List<Student> applicants = new ArrayList<>();
		
		if (allStudents == null) {
	        throw new RuntimeException("Could not retrieve Students from the database.");
	    }
		
	    for (Student student : allStudents) {
	        if (student.getLookingForTraineeship()) {
	            applicants.add(student);
	        }
	    }
	    
		return applicants;
	}
	

	@Override
	public List<TraineeshipPosition> showMathingTraineeships(String username, String strategy) {
	    Optional<Student> optStudent = studentMapper.findById(username);
	    
	    if (optStudent.isEmpty()) {
	        throw new EntityNotFoundException("Student not found.");
	    }
	    
	    Student student = optStudent.get();

	    return traineeshipSearchService.findMatchingPositions(strategy, student);
	}

	
	@Override
	public void assignTraineeship(Integer positionID, String username) {
		Optional<TraineeshipPosition> optionalPosition = trainMapper.findById(positionID);
	    Optional<Student> optionalStudent = studentMapper.findById(username);
	    
	    if (optionalPosition.isEmpty()) {
	        throw new EntityNotFoundException("Traineeship position with ID " + positionID + " not found.");
	    }
	    
	    if (optionalStudent.isEmpty()) {
	        throw new EntityNotFoundException("Student with username " + username + " not found.");
	    }
	    
	    TraineeshipPosition position = optionalPosition.get();
	    Student student = optionalStudent.get();
	    
	    // Check if the traineeship position is already assigned
	    if (position.isAssigned()) {
	        throw new IllegalStateException("Traineeship position is already assigned.");
	    }
	    
	    // Check if the student is already assigned to a traineeship
	    if (student.getAssignedTraineeship() != null) {
	        throw new IllegalStateException("Student is already assigned to another traineeship.");
	    }
	    
	    // Assign the student to the position and mark it as assigned
	    position.setStudent(student);
	    position.setAssigned(true);
	    
	    // Set the assigned traineeship in the student entity
	    student.setAssignedTraineeship(position);
	    student.setLookingForTraineeship(false);
	    
	    trainMapper.save(position);
	    studentMapper.save(student);
	}
	

	@Override
	public List<TraineeshipPosition> showTraineeshipsInProgress() {
		List<TraineeshipPosition> allPositions = trainMapper.findAll();
		List<TraineeshipPosition> inProgress = new ArrayList<>();

	    if (allPositions == null) {
	        throw new RuntimeException("Failed to retrieve traineeship positions from the database.");
	    }
	    
	    for (TraineeshipPosition position : allPositions) {
	        if (position.isAssigned()) {
	            inProgress.add(position);
	        }
	    }
	    
	    return inProgress;
	}
	
	
	@Override
	public List<Professor> showAvailableProfessors(String strategy, Integer positionId) {
		
		return professorSelectionService.selectProfessors(strategy, positionId);
	}
	

	@Override
	public void assignSupervisingProfessor(Integer positionID, String username) {
		Optional<TraineeshipPosition> optionalPosition = trainMapper.findById(positionID);
	    Optional<Professor> optionalProfessor = profMapper.findById(username);
		
	    if (optionalPosition.isEmpty()) {
	        throw new EntityNotFoundException("Traineeship position with ID " + positionID + " not found.");
	    }
	    
	    if (optionalProfessor.isEmpty()) {
	        throw new EntityNotFoundException("Professor with username " + username + " not found.");
	    }
	    
	    TraineeshipPosition position = optionalPosition.get();
	    Professor professor = optionalProfessor.get();
	    
	    // Ensure the traineeship is in progress
	    if (!position.isAssigned()) {
	        throw new IllegalStateException("Traineeship position is not assigned to any student.");
	    }
	    
	    // Ensure supervisor is not already assigned
	    if (position.getSupervisor() != null) {
	        throw new IllegalStateException("This traineeship already has a supervising professor assigned.");
	    }
	    
	    // Assign the supervising professor
	    position.setSupervisor(professor);
	    trainMapper.save(position);
	    
	    professor.addPosition(position);
	    profMapper.save(professor);
	}

	
	@Override
	public List<Evaluation> monitorTraineeship(Integer positionID) {
	    Optional<TraineeshipPosition> optionalPosition = trainMapper.findById(positionID);

	    if (optionalPosition.isEmpty()) {
	        throw new EntityNotFoundException("Traineeship position with ID " + positionID + " not found.");
	    }

	    TraineeshipPosition position = optionalPosition.get();
	    return position.getEvaluations();  
	}


	@Override
	public void terminateTraineeship(Integer positionID, boolean grade) {
	    Optional<TraineeshipPosition> optionalPosition = trainMapper.findById(positionID);

	    if (optionalPosition.isEmpty()) {
	        throw new EntityNotFoundException("Traineeship position with ID " + positionID + " not found.");
	    }

	    TraineeshipPosition position = optionalPosition.get();

	    Professor professor = position.getSupervisor();
	    Student student = position.getStudent();
	    
	    
	    List<TraineeshipPosition> profPositions = professor.getPositions();
	    profPositions.remove(position);
	    professor.setPositions(profPositions);
	    
	    student.setAssignedTraineeship(null);
	    student.setLookingForTraineeship(false);
	    
	    position.setStudent(null);
	    position.setSupervisor(null);
	    position.setAssigned(false);
	    position.setStudentLogbook(null);
	    position.getEvaluations().clear(); 
	    
	    position.setPassFailGrade(grade);
	    
	    profMapper.save(professor);
	    studentMapper.save(student);
	    trainMapper.save(position);
	}

	
	@Override
    public Optional<CommitteeMember> findByUsername(String username) {
        return comMapper.findByUsername(username);
	}
	
}