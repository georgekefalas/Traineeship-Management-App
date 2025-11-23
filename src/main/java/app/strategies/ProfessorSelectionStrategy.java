package app.strategies;

import app.entities.Professor;
import java.util.List;

public interface ProfessorSelectionStrategy {
    
	List<Professor> selectProfessors();
}
