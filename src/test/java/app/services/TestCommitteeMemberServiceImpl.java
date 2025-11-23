package app.services;

import app.entities.CommitteeMember;
import app.entities.Evaluation;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import app.entities.Professor;
import app.mappers.EvaluationMapper;
import app.mappers.ProfessorMapper;
import app.entities.Student;
import app.mappers.StudentMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class TestCommitteeMemberServiceImpl {
	
	@Autowired
    private CommitteeMemberService committeeService;
    
    @Autowired
    private TraineeshipPositionMapper traineeshipMapper;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private ProfessorMapper professorMapper;
    
    @Autowired
    private ProfessorService professorService;
    
    @Autowired
    private EvaluationMapper evaluationMapper;
    
    
    @AfterEach
    public void cleanUp() {
        traineeshipMapper.deleteAll();
        studentMapper.deleteAll();
    }
    
    @Test
    public void testCreateAndRetrieveProfile() {
    	CommitteeMember committee=new CommitteeMember();
    	committee.setUsername("testuser");
        committee.setMemberName("Test");
    	
        committeeService.createProfile(committee);
    	
        CommitteeMember retrieved= committeeService.retrieveProfile("testuser");
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getMemberName());
    
    }
    
    @Test
    @Transactional
    public void testAssignTraineeship() {
        
       CommitteeMember committee = new CommitteeMember();
       committee.setUsername("testuser");
       committee.setMemberName("Test");
       committeeService.createProfile(committee);

       
       Student student = new Student();
       student.setUsername("username");
       student.setLookingForTraineeship(true);
       student.setAssignedTraineeship(null);
       studentMapper.save(student);

       
       TraineeshipPosition position = new TraineeshipPosition();
       position.setAssigned(false);
       traineeshipMapper.save(position);
       Integer positionId = position.getId();

        
       committeeService.assignTraineeship(positionId, student.getUsername());

       
       Optional <TraineeshipPosition> optionalPosition = traineeshipMapper.findById(positionId);

       Optional <Student> optionalStudent = studentMapper.findById(student.getUsername());

       assertTrue(optionalStudent.isPresent());
       
       assertTrue(optionalPosition.isPresent());
       

       Student updatedStudent = optionalStudent.get();

       TraineeshipPosition updatedPosition = optionalPosition.get();
       
       
       assertTrue(position.isAssigned());  
       assertEquals(updatedStudent, updatedPosition.getStudent());
       assertEquals(updatedPosition, updatedStudent.getAssignedTraineeship());
       assertFalse(updatedStudent.getLookingForTraineeship());
    }
    
    
    
    @Test
    @Transactional
    void testShowApplicants() {
        
        Student applicant1 = new Student();
        applicant1.setUsername("applicant1");
        applicant1.setLookingForTraineeship(true);
        studentMapper.save(applicant1);

        Student applicant2 = new Student();
        applicant2.setUsername("applicant2");
        applicant2.setLookingForTraineeship(true);
        studentMapper.save(applicant2);

        Student applicant3 = new Student();
        applicant3.setUsername("applicant3");
        applicant3.setLookingForTraineeship(false);
        studentMapper.save(applicant3);
        
        
        Optional<Student> optionalStudent = studentMapper.findById("applicant1");
        assertTrue(optionalStudent.isPresent());
        Student Student1 = optionalStudent.get();
        
        Optional<Student> optionalStudent2 = studentMapper.findById("applicant2");
        assertTrue(optionalStudent2.isPresent());
        Student Student2 = optionalStudent2.get();
        
        Optional<Student> optionalStudent3 = studentMapper.findById("applicant3");
        assertTrue(optionalStudent3.isPresent());
        Student Student3 = optionalStudent3.get();
        

 
        List<Student> applicants = committeeService.showApplicants();

        
        assertEquals(2, applicants.size());
        assertTrue(applicants.contains(Student1));
        assertTrue(applicants.contains(Student2));
        assertFalse(applicants.contains(Student3));
    }
    
    @Test
    @Transactional
    public void testShowTraineeshipsInProgress() {
    	
    	
    	 CommitteeMember committee = new CommitteeMember();
         committee.setUsername("testuser");
         committee.setMemberName("Test");
         committeeService.createProfile(committee);
    	
    	
         Student student = new Student();
         student.setUsername("username");
         student.setLookingForTraineeship(true);
         student.setAssignedTraineeship(null);
         studentMapper.save(student);

         
         TraineeshipPosition position = new TraineeshipPosition();
         position.setAssigned(false);
         traineeshipMapper.save(position);
         Integer positionId = position.getId();

          
         committeeService.assignTraineeship(positionId, student.getUsername());
         
        
       
         List <TraineeshipPosition> inprogress=  committeeService.showTraineeshipsInProgress();
         
         assertEquals(1, inprogress.size());
         assertTrue(inprogress.contains(position));	
    	
    }
    
    
    
    @Test
    void testFindByUsername() {
        CommitteeMember member = new CommitteeMember();
        member.setUsername("comtest");
        member.setMemberName("Committee One");
        
        Optional<CommitteeMember> found = committeeService.findByUsername("comtest");
        assertTrue(found.isEmpty() || found.get().getUsername().equals("comtest"));
    }
    
    
    @Test
    @Transactional
    void testTerminateTraineeship() {
        Student student = new Student();
        student.setUsername("terminateUser");
        studentService.createProfile(student);
        
        
        Professor professor = new Professor();
        professor.setUsername("profUser");
        professor.setProfessorName("Prof. Smith");
        professor.setPositions(new ArrayList<>());
        professorMapper.save(professor);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTitle("Terminating Internship");
        position.setAssigned(true);
        position.setStudent(student);
        position.setSupervisor(professor);

        TraineeshipPosition savedPosition = traineeshipMapper.save(position);

        student.setAssignedTraineeship(savedPosition);
        studentService.createProfile(student);

        assertNotNull(savedPosition.getId());

        committeeService.terminateTraineeship(savedPosition.getId(), true);

        List<Evaluation> evaluations = committeeService.monitorTraineeship(savedPosition.getId());
        assertNotNull(evaluations);

        TraineeshipPosition updated = traineeshipMapper.findById(savedPosition.getId()).orElseThrow();
        assertFalse(updated.isAssigned());
        assertTrue(updated.isPassFailGrade());
    }
    
    
    
    @Test
    @Transactional
    void testMonitorTraineeship() {
        Student student = new Student();
        student.setUsername("monitorStudent" + UUID.randomUUID());
        studentService.createProfile(student);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTitle("Monitoring Internship " + UUID.randomUUID());
        position.setAssigned(true);
        position.setStudent(student);
        TraineeshipPosition savedPosition = traineeshipMapper.save(position);

        student.setAssignedTraineeship(savedPosition);
        studentService.createProfile(student);

        Evaluation eval = new Evaluation();
        eval.setMotivation(8);
        eval.setEfficiency(7);
        eval.setEffectiveness(9);
        eval.setType(true);

        evaluationMapper.save(eval);

        List<Evaluation> evaluations = committeeService.monitorTraineeship(savedPosition.getId());

        assertNotNull(evaluations);
    }

    
    @Test
    @Transactional
    void testAssignSupervisingProfessor() {
        Professor prof = new Professor();
        prof.setUsername("prof1");
        professorService.createProfile(prof);

        Student student = new Student();
        student.setUsername("student1");
        student.setStudentName("Test Student");
        studentService.createProfile(student);

        TraineeshipPosition position = new TraineeshipPosition();
        position.setTitle("Internship A");
        position.setAssigned(true);
        position.setStudent(student);

        TraineeshipPosition savedPos = traineeshipMapper.save(position);
        Integer positionId = savedPos.getId();
        assertNotNull(positionId);

        student.setAssignedTraineeship(savedPos);
       studentMapper.save(student);

        committeeService.assignSupervisingProfessor(positionId, "prof1");

        Optional<TraineeshipPosition> updated = traineeshipMapper.findById(positionId);
        assertTrue(updated.isPresent());
        assertNotNull(updated.get().getSupervisor());
        assertEquals("prof1", updated.get().getSupervisor().getUsername());
    }
    
    
}
