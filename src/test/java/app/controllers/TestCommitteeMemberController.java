package app.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import app.entities.*;
import app.mappers.*;
import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
  locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class TestCommitteeMemberController {
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CommitteeMemberController comemberController;
	
	@Autowired
    private CommitteeMemberMapper comemberMapper;
	
	@Autowired
    private StudentMapper studentMapper;
	
	@Autowired
    private TraineeshipPositionMapper trainMapper;
	
	@Autowired
    private CompanyMapper companyMapper;
	
	@Autowired
    private ProfessorMapper profMapper;
	
	
	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
    }
	
	@AfterEach
	public void cleanup() {
	    studentMapper.deleteAll();
	    companyMapper.deleteAll();
	    trainMapper.deleteAll();
	}
	
	@Test
	public void testCompanyControllerIsNotNull() {
		Assertions.assertNotNull(comemberController);
	}
	
	@Test
	public void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
	
	@Test
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testShowDashboard() throws Exception {
        mockMvc.perform(get("/committee/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("committee/dashboard"));
    }
	
	@Test
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/committee/create_form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("committeeMember"))
                .andExpect(view().name("committee/create_form"));
    }
	
	@Test
	@Transactional
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testRetrieveProfile() throws Exception {
        CommitteeMember committee = new CommitteeMember();
        committee.setUsername("testmem");
        comemberMapper.save(committee);

        mockMvc.perform(get("/committee/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("committeeMember"))
                .andExpect(view().name("committee/profile"));
    }
	
	
	@Test
	@Transactional
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testShowApplicants() throws Exception {
        Student student = new Student();
        student.setUsername("applicant1");
        student.setStudentName("Applicant One");
        student.setAssignedTraineeship(null);
        studentMapper.save(student);

        mockMvc.perform(get("/committee/applicants"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(view().name("committee/applicants"));
    }
	
	
	
	@Test
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testShowMatchingTraineeships() throws Exception {
		
		Student s = new Student();
	    s.setUsername("matchStudent");
	    studentMapper.save(s);

	    Company c = new Company();
	    c.setUsername("matchingCompany");
	    c.setCompanyName("Company XYZ");
	    companyMapper.save(c);

	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("Matching Internship");
	    pos.setAssigned(false);
	    pos.setCompany(c);
	    trainMapper.save(pos);

	    mockMvc.perform(get("/committee/matching-traineeships/{username}", "matchStudent"))
	            .andExpect(status().isOk())
	            .andExpect(model().attributeExists("positions"))
	            .andExpect(model().attributeExists("studentUsername"))
	            .andExpect(view().name("committee/matching_traineeships"));
    }
	
	
	@Test
	@Transactional
    @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
    public void testAssignTraineeship() throws Exception {
        Student student = new Student();
        student.setUsername("assignUser");
        student.setStudentName("Assign Target");
        student.setAssignedTraineeship(null);
        studentMapper.save(student);

        Company company = new Company();
        company.setUsername("comp1");
        company.setCompanyName("Test Company");
        companyMapper.save(company);

        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTitle("Assignment Test");
        pos.setAssigned(false);
        pos.setCompany(company);
        trainMapper.save(pos);

        mockMvc.perform(get("/committee/assign-traineeship")
                .param("positionId", pos.getId().toString())
                .param("studentUsername", "assignUser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/committee/applicants"));
    }
	
	
	 @Test
	 @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
	 public void testShowInProgress() throws Exception {
	    Company company = new Company();
	    company.setUsername("compXYZ");
	    company.setCompanyName("InProgressCompany");
	    companyMapper.save(company);
	    
	    Student student = new Student();
	    student.setUsername("inProgressUser2");
	    student.setStudentName("Progress Tester");
	    studentMapper.save(student);

	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("In Progress Position");
	    pos.setAssigned(true);
	    pos.setCompany(company);
	    pos.setStudent(student);
	    trainMapper.save(pos);


	    mockMvc.perform(get("/committee/in-progress"))
	           .andExpect(status().isOk())
	           .andExpect(model().attributeExists("positions"))
	           .andExpect(view().name("committee/in_progress"));
     }
	 
	 
	 @Test
	 @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
	 public void testShowAvailableProfessors() throws Exception {
	    Professor professor = new Professor();
	    professor.setUsername("prof123");
	    professor.setProfessorName("Prof One");
	    profMapper.save(professor);

	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("Need Supervisor");
	    pos.setAssigned(true);
	    trainMapper.save(pos);

	    mockMvc.perform(get("/committee/available-professors")
	           .param("positionId", pos.getId().toString()))
	           .andExpect(status().isOk())
	           .andExpect(model().attributeExists("professors"))
	           .andExpect(model().attributeExists("positionId"))
	           .andExpect(view().name("committee/available_professors"));
	    }
	
	 @Test
	 @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
	 public void testAssignSupervisor() throws Exception {
	    Professor prof = new Professor();
	    prof.setUsername("sup123");
	    prof.setProfessorName("Supervisor X");
	    profMapper.save(prof);

	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("Supervised Internship");
	    pos.setAssigned(true);
	    trainMapper.save(pos);

	   mockMvc.perform(get("/committee/assign-supervisor")
	          .param("positionId", pos.getId().toString())
	          .param("professorUsername", "sup123"))
	          .andExpect(status().is3xxRedirection())
	          .andExpect(redirectedUrl("/committee/in-progress"));
	    }	
	
	 
	 @Test
	 @Transactional
	 @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
	  public  void testMonitorTraineeship() throws Exception {
	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("Monitor Test");
	    pos.setAssigned(true);
	    pos.setEvaluations(new ArrayList<>());
	    trainMapper.save(pos);

	    mockMvc.perform(get("/committee/monitor/" + pos.getId()))
	           .andExpect(status().isOk())
	           .andExpect(model().attributeExists("evaluations"))
	           .andExpect(model().attributeExists("positionId"))
	           .andExpect(view().name("committee/monitor"));
	    }

	  @Test
	  @Transactional
	  @WithMockUser(username = "testmem", roles = {"COMMITTEEMEMBER"})
	  public void testTerminateTraineeship() throws Exception {
		  
		Professor prof = new Professor();
		prof.setUsername("professorX");
		profMapper.save(prof);  
		
		Student student = new Student();
	    student.setUsername("studentY");
	    studentMapper.save(student);
		  
	    TraineeshipPosition pos = new TraineeshipPosition();
	    pos.setTitle("Terminate Me");
	    pos.setAssigned(true);
	    pos.setSupervisor(prof);
	    pos.setStudent(student); 
	    trainMapper.save(pos);

	   mockMvc.perform(get("/committee/terminate/{positionId}" ,pos.getId())
	          .param("result", "pass"))
	          .andExpect(status().is3xxRedirection())
	          .andExpect(redirectedUrl("/committee/in-progress"));
	    }


}
