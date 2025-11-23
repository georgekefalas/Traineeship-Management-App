package app.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Student;

@Repository
public interface StudentMapper extends JpaRepository<Student, String> {
	Optional<Student> findByUsername(String username);
}