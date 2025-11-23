package app.services;

import app.entities.Evaluation;
import app.entities.Professor;
import app.entities.TraineeshipPosition;
import app.mappers.ProfessorMapper;
import app.mappers.TraineeshipPositionMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.Optional;


@SpringBootTest
public class TestProfessorServiceImpl {
	
	
	 @Autowired
	 private ProfessorService profService;
	 
	 @Autowired
	 private ProfessorMapper profMapper;
	    
	 @Autowired
	 private TraineeshipPositionMapper traineeshipMapper;


	 @Test
	 public void testCreateAndRetrieveProfile() {
	        Professor prof = new Professor();
	        prof.setUsername("testuser");
	        prof.setProfessorName("Test");

	        profService.createProfile(prof);

	        Professor retrieved = profService.retrieveProfile("testuser");
	        assertNotNull(retrieved);
	        assertEquals("Test", retrieved.getProfessorName());
	    }


	 @Test
	 @Transactional
	 public void testShowSupervisingPositions() {
	        Professor prof = new Professor();
	        prof.setUsername("testuser");
	        prof.setProfessorName("Test");

	        profService.createProfile(prof);
	        
	        TraineeshipPosition pos = new TraineeshipPosition();
	        pos.setTitle("Job");
	        pos.setSupervisor(prof);
	       
	        List<TraineeshipPosition> positions = new ArrayList<>();
	        positions.add(pos);
	        prof.setPositions(positions);
	        
	        profMapper.save(prof);
	        
	        traineeshipMapper.save(pos);
	       
	        Professor retrieved = profService.retrieveProfile("testuser");
	        assertNotNull(retrieved);
	        
	        List<TraineeshipPosition> retrievedPositions = retrieved.getPositions();
	        assertNotNull(retrievedPositions);
	        assertEquals(1, retrievedPositions.size());
	        assertEquals("Job", retrievedPositions.get(0).getTitle());

	 }

	

	 @Test
	 @Transactional
	 public void testFindByUsername() {
	        Professor prof = new Professor();
	        prof.setUsername("lookupuser");
	        prof.setProfessorName("Lookup User");

	        profService.createProfile(prof);

	        Optional<Professor> found = profService.findByUsername("lookupuser");
	        assertTrue(found.isPresent());
	        assertEquals("Lookup User", found.get().getProfessorName());
	 }
	
	 @Test
	 @Transactional
	 public void testEvaluateTraineeship() {
	     
	     Professor prof = new Professor();
	     prof.setUsername("prof123");
	     prof.setProfessorName("Smith");
	     profMapper.save(prof); 

	 
	     TraineeshipPosition position = new TraineeshipPosition();
	     position.setTitle("Internship A");
	     position.setSupervisor(prof);
	     traineeshipMapper.save(position); 

	     
	     Evaluation evaluation = new Evaluation();
	     evaluation.setEffectiveness(5);

	     
	     profService.evaluateTraineeship(position.getId(), evaluation);
	     
	     Optional<TraineeshipPosition> retrievedOpt = traineeshipMapper.findById(position.getId());
	     assertTrue(retrievedOpt.isPresent());

	     TraineeshipPosition retrieved = retrievedOpt.get();
	     List<Evaluation> evaluations = retrieved.getEvaluations();

	     assertNotNull(evaluations);
	     assertEquals(1, evaluations.size());
	     assertEquals(5, evaluations.get(0).getEffectiveness());
	 }


}
