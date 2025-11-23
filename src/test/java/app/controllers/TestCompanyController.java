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
import app.entities.Company;
import app.mappers.CompanyMapper;


@SpringBootTest
@TestPropertySource(
  locations = "classpath:application.properties")
@AutoConfigureMockMvc
public class TestCompanyController {
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CompanyController companyController;
	
	@Autowired
    private CompanyMapper companyMapper;
    
	
	
	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
    }
	
	@Test
	public void testCompanyControllerIsNotNull() {
		Assertions.assertNotNull(companyController);
	}
	
	@Test
	public void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
	
	
	@Test
    @WithMockUser(username = "testcompany", roles = {"COMPANY"})
    public void testShowDashboard() throws Exception {
        mockMvc.perform(get("/company/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("company/dashboard"));
    }
	
	
	@Test
    @WithMockUser(username = "testcompany", roles = {"COMPANY"})
    public void testRetrieveProfile() throws Exception {
        Company company = new Company();
        company.setUsername("testcompany");
        company.setCompanyName("AE");
        companyMapper.save(company);

        mockMvc.perform(get("/company/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("company"))
                .andExpect(view().name("company/profile"));
    }
	
	@Test
    @WithMockUser(username = "testcompany", roles = {"COMPANY"})
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/company/create_form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("company"))
                .andExpect(view().name("company/create_form"));
    }
	
	@Test
    @WithMockUser(username = "testcompany", roles = {"COMPANY"})
	public void testShowAddPositionForm() throws Exception {
	    mockMvc.perform(get("/company/addposition"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("company/pos_form")) // Adjust this if the view name is different
	            .andExpect(model().attributeExists("traineeshipPosition"));
	}
	
	@Test
	@WithMockUser(username = "testcompany", roles = {"COMPANY"})
	public void testShowEvaluationForm() throws Exception {

	    mockMvc.perform(get("/company/evaluate_form/{positionId}", 1))
	            .andExpect(status().isOk())
	            .andExpect(view().name("company/evaluate_form"))
	            .andExpect(model().attributeExists("evaluation"))
	            .andExpect(model().attributeExists("positionId"));
	}
	
	
	@Test
	@WithMockUser(username = "testcompany", roles = {"COMPANY"})
	public void testshowAvailablePositions() throws Exception {

	    mockMvc.perform(get("/company/availablepositions"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("company/availablepositions"))
	            .andExpect(model().attributeExists("positions"));
	}
	
	@Test
	@WithMockUser(username = "testcompany", roles = {"COMPANY"})
	public void testshowAssignedPositions() throws Exception {

	    mockMvc.perform(get("/company/assignedpositions"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("company/assignedpositions"))
	            .andExpect(model().attributeExists("positions"));
	}	
		

}
