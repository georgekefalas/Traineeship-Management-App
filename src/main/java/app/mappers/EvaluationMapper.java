package app.mappers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Evaluation;

@Repository
public interface EvaluationMapper extends JpaRepository<Evaluation, Integer> {

}