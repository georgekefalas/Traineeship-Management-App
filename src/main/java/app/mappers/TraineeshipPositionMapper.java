package app.mappers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.TraineeshipPosition;

@Repository
public interface TraineeshipPositionMapper extends JpaRepository<TraineeshipPosition, Integer> {
	
}