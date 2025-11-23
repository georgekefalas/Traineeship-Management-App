package app.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Professor;

@Repository
public interface ProfessorMapper extends JpaRepository<Professor, String> {
	
	Optional<Professor> findByUsername(String username);
	
}