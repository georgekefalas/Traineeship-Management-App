package app.controllers;

import app.entities.Role;
import app.entities.User;
import app.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void testRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("auth/register"));
    }

    @Test
    void testRegisterNewUser() throws Exception {
        mockMvc.perform(post("/save")
                .param("username", "newuser")
                .param("password", "1234")
                .param("role", "STUDENT")
        ).andExpect(status().isOk())
         .andExpect(view().name("auth/login"));

    }

    @Test
    void testRegisterExistingUser() throws Exception {
        if (!userMapper.findById("existinguser").isPresent()) {
            User u = new User();
            u.setUsername("existinguser");
            u.setPassword("1234");
            u.setRole(Role.STUDENT);
            userMapper.save(u);
        }

        mockMvc.perform(post("/save")
                .param("username", "existinguser")
                .param("password", "4321")
                .param("role", "STUDENT")
        ).andExpect(status().isOk())
         .andExpect(view().name("auth/login"))
         .andExpect(model().attributeExists("successMessage"));
    }
}