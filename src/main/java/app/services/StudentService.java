package app.services;

import java.util.Optional;

import app.entities.*;


public interface StudentService {
    
    public void createProfile(Student student);
    
    public Student retrieveProfile(String username);

    public void applyForTraineeship(String username);

    public void fillLogbook(Integer traineeshipid, String logbookContent);
    
    public Optional<Student> findByUsername(String username);
    
}
