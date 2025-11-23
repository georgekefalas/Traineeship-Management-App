package app.strategies;

import app.entities.Student;
import app.entities.TraineeshipPosition;
import java.util.*;

public interface TraineeshipSearchStrategy {
    List<TraineeshipPosition> findMatchingPositions(Student student);
}
