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
import app.entities.Professor;
import app.mappers.ProfessorMapper;

@SpringBootTest
@TestPropertySource(
  locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class TestProfessorController {


	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ProfessorController profController;
	
	@Autowired
    private ProfessorMapper profMapper;


	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
    }
	
	@Test
	public void testProfessorControllerIsNotNull() {
		Assertions.assertNotNull(profController);
	}
	
	@Test
	public void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}


	@Test
    @WithMockUser(username = "testprof", roles = {"PROFESSOR"})
    public void testShowDashboard() throws Exception {
        mockMvc.perform(get("/professor/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("professor/dashboard"));
    }
	

	@Test
    @WithMockUser(username = "testprof", roles = {"PROFESSOR"})
    public void testRetrieveProfile() throws Exception {
        Professor prof = new Professor();
        prof.setUsername("testprof");
        profMapper.save(prof);

        mockMvc.perform(get("/professor/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("professor"))
                .andExpect(view().name("professor/profile"));
    }
	
	@Test
    @WithMockUser(username = "testprof", roles = {"PROFESSOR"})
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/professor/create_form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("professor"))
                .andExpect(view().name("professor/create_form"));
    }


	@Test
	@WithMockUser(username = "testprof", roles = {"PROFESSOR"})
	public void testShowEvaluationForm() throws Exception {

	    mockMvc.perform(get("/professor/evaluate_form/{positionId}", 1))
	            .andExpect(status().isOk())
	            .andExpect(view().name("professor/evaluate_form"))
	            .andExpect(model().attributeExists("evaluation"))
	            .andExpect(model().attributeExists("positionId"));
	}

	@Test
	@WithMockUser(username = "testprof", roles = {"PROFESSOR"})
	public void testShowSupervisingPositions() throws Exception {

	    mockMvc.perform(get("/professor/positions"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("professor/positions"))
	            .andExpect(model().attributeExists("positions"));
	}


}
