package app.services;

import app.entities.Role;
import app.entities.User;
import app.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testCreateUserAndCheckPasswordEncoding() {
        User user = new User();
        user.setUsername("junituser");
        user.setPassword("plainPassword");
        user.setRole(Role.STUDENT);

        userService.createUser(user);

        User savedUser = userMapper.findById("junituser").orElse(null);
        assertNotNull(savedUser);
        assertNotEquals("plainPassword", savedUser.getPassword());
    }

    @Test
    void testIsUserPresent() {
        User user = new User();
        user.setUsername("checkUser");
        user.setPassword("secret");
        user.setRole(Role.STUDENT);
        
        userService.createUser(user);

        assertTrue(userService.isUserPresent(user));
    }

    @Test
    void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("loginUser");
        user.setPassword("mypassword");
        user.setRole(Role.STUDENT);
        
        userService.createUser(user);

        UserDetails userDetails = ((UserDetailsService) userService).loadUserByUsername("loginUser");
        assertNotNull(userDetails);
        assertEquals("loginUser", userDetails.getUsername());
    }
}
