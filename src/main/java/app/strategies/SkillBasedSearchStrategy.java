package app.strategies;

import app.entities.Student;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("skillBasedStrategy")
public class SkillBasedSearchStrategy implements TraineeshipSearchStrategy {
	
	
	@Autowired
    private TraineeshipPositionMapper trainMapper;
	
	
	@Override
	public List<TraineeshipPosition> findMatchingPositions(Student student) {

	    List<TraineeshipPosition> allAvailable = trainMapper.findAll();

	    List<String> studentSkills = student.getSkills().stream()
	            .map(String::toLowerCase)
	            .collect(Collectors.toList());

	    return allAvailable.stream()
	            .filter(position -> studentMatchesSkills(
	                    studentSkills,
	                    position.getSkills().stream()
	                            .map(String::toLowerCase)
	                            .collect(Collectors.toList())
	            ))
	            .collect(Collectors.toList());
	}
	
	
	private boolean studentMatchesSkills(List<String> studentSkills, List<String> requiredSkills) {
        return requiredSkills.stream().allMatch(skill -> studentSkills.contains(skill));
    }
}
