package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entities.*;
import jakarta.persistence.EntityNotFoundException;
import app.mappers.*;

import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

	
	@Autowired
	private StudentMapper studentMapper;
	
	@Autowired
	private TraineeshipPositionMapper trainMapper;


    // Constructors
	
	public StudentServiceImpl() {
		
	}
	
    @Autowired
	public StudentServiceImpl(StudentMapper studentMapper) {
		this.studentMapper = studentMapper;
	}

    
    // Methods
	
	@Override
	public void createProfile(Student student) {		
		studentMapper.save(student);
	}
	

    @Override
	public Student retrieveProfile(String username) {
		
		Optional<Student> student = studentMapper.findById(username);
		
		if (student.isPresent())
			return student.get();
		else
			return null;
	}

    
    @Override
    public void applyForTraineeship(String studentName) {
        Optional<Student> optionalStudent = studentMapper.findById(studentName);
        
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            
            boolean status = student.getLookingForTraineeship();

            student.setLookingForTraineeship(!status);            
            studentMapper.save(student);
        } 
        else {
            throw new EntityNotFoundException("Student with ID " + studentName + " not found.");
        }
    }
	

    @Override
    public void fillLogbook(Integer traineeshipId, String logbookContent) {
        Optional<TraineeshipPosition> optionalPosition = trainMapper.findById(traineeshipId);
        if (optionalPosition.isPresent()) {
            TraineeshipPosition position = optionalPosition.get();
            position.setStudentLogbook(logbookContent);
            trainMapper.save(position);
        } else {
            throw new EntityNotFoundException("TraineeshipPosition with ID " + traineeshipId + " not found.");
        }
    }
    
    
    @Override
    public Optional<Student> findByUsername(String username) {
        return studentMapper.findByUsername(username);
    }

}