package app.services;

import app.entities.Company;
import app.entities.Evaluation;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class TestCompanyServiceImpl {
	
	@Autowired
    private CompanyService companyService;
    
    @Autowired
    private TraineeshipPositionMapper traineeshipMapper;
	
	
    @Test
    public void testCreateAndRetrieveProfile() {
        Company comp = new Company();
        comp.setUsername("testuser");
        comp.setCompanyName("Test");

        companyService.createProfile(comp);

        Company retrieved = companyService.retrieveProfile("testuser");
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getCompanyName());
    }
    
    
    @Test 
    @Transactional
    public void testAddPositions() {

        Company comp = new Company();
        TraineeshipPosition pos = new TraineeshipPosition();

        comp.setUsername("vaggelis");
        comp.setCompanyName("Mega");

        
        comp.setPositions(new ArrayList<>());

        pos.setTitle("ardaki");

        companyService.createProfile(comp);

        companyService.addPosition("vaggelis", pos);

        Optional<Company> found = companyService.findByUsername("vaggelis");
        assertTrue(found.isPresent());

        assertEquals("ardaki", found.get().getPositions().get(0).getTitle());
    }
    
    
    @Test
    @Transactional
    public void testDeletePositions() {
        
        Company comp = new Company();
        comp.setUsername("alafou");
        comp.setCompanyName("Skai");
        comp.setPositions(new ArrayList<>());

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTitle("ardaki");

        companyService.createProfile(comp);

        
        companyService.addPosition("alafou", pos);

      
        Optional<Company> found = companyService.findByUsername("alafou");
        assertTrue(found.isPresent());

      
        Integer positionId = found.get().getPositions().get(0).getId();
        assertEquals("ardaki", found.get().getPositions().get(0).getTitle());

       
        companyService.deletePosition("alafou", positionId);

       
        Optional<Company> afterDelete = companyService.findByUsername("alafou");
        assertTrue(afterDelete.isPresent());

       
        assertTrue(afterDelete.get().getPositions().isEmpty());
    }
    
    @Test
    @Transactional
    public void testShowAvailablePositions() {
        Company comp = new Company();
        comp.setUsername("alafou");
        comp.setCompanyName("Skai");
        comp.setPositions(new ArrayList<>());
        
        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setTitle("ardaki");
        pos1.setAssigned(false);
        
        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setTitle("not available");
        pos2.setAssigned(true);
        
        companyService.createProfile(comp);
        companyService.addPosition("alafou", pos1);
        companyService.addPosition("alafou", pos2);
        
        List<TraineeshipPosition> availablePositions = companyService.showAvailablePositions("alafou");
        
        assertEquals(1, availablePositions.size());
        assertEquals("ardaki", availablePositions.get(0).getTitle());
    }
    
    
    @Test
    @Transactional
    public void testShowAssignedPositions() {
        Company comp = new Company();
        comp.setUsername("alafou");
        comp.setCompanyName("Skai");
        comp.setPositions(new ArrayList<>());
        
        TraineeshipPosition pos1 = new TraineeshipPosition();
        pos1.setTitle("ardaki");
        pos1.setAssigned(true);
        
        TraineeshipPosition pos2 = new TraineeshipPosition();
        pos2.setTitle("not available");
        pos2.setAssigned(true);
        
        companyService.createProfile(comp);
        companyService.addPosition("alafou", pos1);
        companyService.addPosition("alafou", pos2);
        
        List<TraineeshipPosition> assignedPositions = companyService.showAssignedPositions("alafou");
        
        assertEquals(2, assignedPositions.size());
        assertEquals("ardaki", assignedPositions.get(0).getTitle());
    }
    
    
    @Test
    @Transactional
	 public void testFindByUsername() {
	        Company comp = new Company();
	        comp.setUsername("lookupuser");
	        comp.setCompanyName("AE");

	        companyService.createProfile(comp);

	        Optional<Company> found = companyService.findByUsername("lookupuser");
	        assertTrue(found.isPresent());
	        assertEquals("AE", found.get().getCompanyName());
	 }
	
    
    @Test
	 @Transactional
	 public void testEvaluateProfessor() {
    	 

	     TraineeshipPosition position = new TraineeshipPosition();
	     position.setTitle("Internship A");
	     traineeshipMapper.save(position); 

	     
	     Evaluation evaluation = new Evaluation();
	     evaluation.setEffectiveness(5);

	     
	     companyService.evaluateTraineeship(position.getId(), evaluation);
	     
	     Optional<TraineeshipPosition> retrievedOpt = traineeshipMapper.findById(position.getId());
	     assertTrue(retrievedOpt.isPresent());

	     TraineeshipPosition retrieved = retrievedOpt.get();
	     List<Evaluation> evaluations = retrieved.getEvaluations();

	     assertNotNull(evaluations);
	     assertEquals(1, evaluations.size());
	     assertEquals(5, evaluations.get(0).getEffectiveness());

	 }
    
    
    @Test
    @Transactional
    public void testShowDetails() {
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTitle("Sample Position");
        traineeshipMapper.save(pos);

        
        TraineeshipPosition result = companyService.showDetails(pos.getId());

        assertNotNull(result);
        assertEquals("Sample Position", result.getTitle());
    }
    

}
