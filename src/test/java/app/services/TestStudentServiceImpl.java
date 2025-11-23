package app.services;

import app.entities.Student;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;


@SpringBootTest
class TestStudentServiceImpl {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TraineeshipPositionMapper traineeshipMapper;

    @Test
    void testCreateAndRetrieveProfile() {
        Student s = new Student();
        s.setUsername("testuser");
        s.setStudentName("Test");

        studentService.createProfile(s);

        Student retrieved = studentService.retrieveProfile("testuser");
        assertNotNull(retrieved);
        assertEquals("Test", retrieved.getStudentName());
    }
    

    @Test
    @Transactional
    void testApplyForTraineeship() {
        Student s = new Student();
        s.setUsername("trainee");
        studentService.createProfile(s);

        studentService.applyForTraineeship("trainee");

        Student updated = studentService.retrieveProfile("trainee");
        assertTrue(updated.getLookingForTraineeship());
    }
    
    @Test
    void testFillLogbook() {  
        TraineeshipPosition pos = new TraineeshipPosition();
        pos.setTitle("Internship");
        traineeshipMapper.save(pos);

        Student s = new Student();
        s.setUsername("logbookUser");
        s.setStudentName("Test User");
        s.setAssignedTraineeship(pos);
        studentService.createProfile(s);

        String content = "Logbook content";
        studentService.fillLogbook(pos.getId(), content);

        Optional<TraineeshipPosition> updated = traineeshipMapper.findById(pos.getId());
        assertTrue(updated.isPresent());
        assertEquals(content, updated.get().getStudentLogbook());
    }
    
    @Test
    void testFindByUsername() {
        Student s = new Student();
        s.setUsername("lookupuser");
        s.setStudentName("Lookup User");

        studentService.createProfile(s);

        Optional<Student> found = studentService.findByUsername("lookupuser");
        assertTrue(found.isPresent());
        assertEquals("Lookup User", found.get().getStudentName());
    }
}