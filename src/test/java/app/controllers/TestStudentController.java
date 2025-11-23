package app.controllers;

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
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import jakarta.transaction.Transactional;
import app.mappers.StudentMapper;
import app.entities.Student;

@SpringBootTest
@TestPropertySource(
  locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class TestStudentController {
	
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private StudentController studentController;
	
	@Autowired
    private TraineeshipPositionMapper traineeMapper;
	
	
	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
    }
	
	@Test
	public void testStudentControllerIsNotNull() {
		Assertions.assertNotNull(studentController);
	}
	
	@Test
	public void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
	
	
	@Test
    @WithMockUser(username = "teststudent", roles = {"STUDENT"})
    public void testShowDashboard() throws Exception {
        mockMvc.perform(get("/student/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/dashboard"));
    }
	
	 @Test
	 @WithMockUser(username = "teststudent", roles = {"STUDENT"})
	  public void testCreateProfile() throws Exception {
	        mockMvc.perform(post("/student/create")
	               .param("studentName", "John Doe"))
	               .andExpect(status().is3xxRedirection())
	               .andExpect(redirectedUrl("/student/dashboard"));
	    }

	
	@Test
	@Transactional
    @WithMockUser(username = "teststudent", roles = {"STUDENT"})
    public void testRetrieveProfile() throws Exception {
        Student student = new Student();
        student.setUsername("teststudent");
        student.setStudentName("Vassilis");
        studentMapper.save(student);

        mockMvc.perform(get("/student/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"))
                .andExpect(view().name("student/profile"));
    }
	
	
	@Test
    @WithMockUser(username = "teststudent", roles = {"STUDENT"})
    public void testCreateForm() throws Exception {
        mockMvc.perform(get("/student/create_form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("student"))
                .andExpect(view().name("student/create_form"));
    }
	
	
	@Test
    @WithMockUser(username = "teststudent", roles = {"STUDENT"})
    public void testApplyForTraineeship() throws Exception {

        mockMvc.perform(get("/student/apply"))
                .andExpect(status().isOk())
                .andExpect(view().name("student/apply"));
    }
	
	 @Test
	 @Transactional
	 @WithMockUser(username = "teststudent", roles = {"STUDENT"})
	 public void testShowLogbook() throws Exception {
		 
		 TraineeshipPosition pos = new TraineeshipPosition();
		 pos.setTitle("Test Position");
		 
		 Student student = new Student();
		 student.setUsername("teststudent");
		 student.setAssignedTraineeship(pos);
		 studentMapper.save(student);
		 
		 mockMvc.perform(get("/student/logbook"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("student/logbook"))
	            .andExpect(model().attributeExists("traineeship"));
	 }
	
	
	 @Test
	 @WithMockUser(username = "teststudent", roles = {"STUDENT"})
	 public void testFillLogbook() throws Exception {
		 TraineeshipPosition pos = new TraineeshipPosition();
		 traineeMapper.save(pos);
		 Integer traineeshipId = pos.getId();
	        mockMvc.perform(post("/student/fill-logbook/{traineeshipId}", traineeshipId)
	                .param("logbookContent", "This is my logbook entry."))
	                .andExpect(status().is3xxRedirection())
	                .andExpect(redirectedUrl("/student/dashboard"));

	    }
	
}
