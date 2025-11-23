package app.strategies;

import app.entities.Company;
import app.entities.Student;
import app.entities.TraineeshipPosition;
import app.mappers.TraineeshipPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("combinedBasedStrategy")
public class CombinedBasedSearchStrategy implements TraineeshipSearchStrategy {

    @Autowired
    private TraineeshipPositionMapper trainMapper;

    private static final double DEFAULT_THRESHOLD = 0.5;

    @Override
    public List<TraineeshipPosition> findMatchingPositions(Student student) {
        
        List<String> studentSkills = student.getSkills().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        List<String> studentInterests = student.getInterests().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        String studentLocation = student.getPreferredLocation();
        if (studentLocation == null) {
            return Collections.emptyList(); // no location preference
        }

        return trainMapper.findAll().stream()
                .filter(position -> studentMatchesSkills(
                        studentSkills,
                        position.getSkills().stream()
                                .map(String::toLowerCase)
                                .collect(Collectors.toList())
                ))
                .filter(position -> {
                    // Location match
                    Company company = position.getCompany();
                    String companyLocation = (company != null && company.getCompanyLocation() != null)
                            ? company.getCompanyLocation().toLowerCase()
                            : "";
                    return studentLocation.toLowerCase().equals(companyLocation);
                })
                .filter(position -> {
                    // Interest match
                    List<String> positionTopics = position.getTopics().stream()
                            .map(String::toLowerCase)
                            .collect(Collectors.toList());
                    double jaccard = computeJaccardSimilarity(studentInterests, positionTopics);
                    return jaccard >= DEFAULT_THRESHOLD;
                })
                .collect(Collectors.toList());
    }

    private boolean studentMatchesSkills(List<String> studentSkills, List<String> requiredSkills) {
        return requiredSkills.stream().allMatch(skill -> studentSkills.contains(skill));
    }

    private double computeJaccardSimilarity(List<String> a, List<String> b) {
        Set<String> setA = new HashSet<>(a);
        Set<String> setB = new HashSet<>(b);

        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
}