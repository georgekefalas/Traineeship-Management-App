package app.strategies;

import app.entities.Company;
import app.entities.Student;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("locationBasedStrategy")
public class LocationBasedSearchStrategy implements TraineeshipSearchStrategy {
	
	
	@Autowired
    private TraineeshipPositionMapper trainMapper;
	
	
	
	@Override
	public List<TraineeshipPosition> findMatchingPositions(Student student) {
	    
	    List<String> studentSkills = student.getSkills().stream()
	        .map(String::toLowerCase)
	        .collect(Collectors.toList());

	    String studentLocation = student.getPreferredLocation();
	    
	    if (studentLocation == null) {
	        return Collections.emptyList(); 
	    }
	    
	    List<TraineeshipPosition> allAvailable = trainMapper.findAll().stream()
	        .filter(position -> studentMatchesSkills(
	            studentSkills,
	            position.getSkills().stream()
	                .map(String::toLowerCase)
	                .collect(Collectors.toList())
	        ))
	        .filter(position -> {
	            Company company = position.getCompany();
	            String companyLocation = (company != null && company.getCompanyLocation() != null)
	                ? company.getCompanyLocation().toLowerCase()
	                : "";
	            return studentLocation.toLowerCase().equals(companyLocation);
	        })
	        .collect(Collectors.toList());

	    return allAvailable;
	}
	
	
	private boolean studentMatchesSkills(List<String> studentSkills, List<String> requiredSkills) {
        return requiredSkills.stream().allMatch(skill -> studentSkills.contains(skill));
    }
	
}
