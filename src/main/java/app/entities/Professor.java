package app.entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name="professor")
public class Professor {
	
	// Fields
	
	@Id
    @Column(name = "username")
    private String username;
	
	@Column(name="professor_name")
	private String professorName;
	
	@ElementCollection
    @CollectionTable(name = "professor_interests", joinColumns = @JoinColumn(name = "professor_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "supervisor")
	private List<TraineeshipPosition> positions=new ArrayList<>();
	
	
	// Constructors
	
	public Professor() {
	}
	
	public Professor(String username, String professorName, List<String> interests, List<TraineeshipPosition> positions) {
		this.username = username;
		this.professorName=professorName;
		this.interests=interests;
		this.positions = positions;
	}

	
	// Getters/Setters
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getProfessorName() {
		return professorName;
	}

	public void setProfessorName(String professorName) {
		this.professorName = professorName;
	}

	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}
	
	
	public String toString() {
		return "Professor [name:" + professorName + "]"; 
	}

	public void setPositions(List<TraineeshipPosition> positions) {
		this.positions = positions;
	}

	public List<TraineeshipPosition> getPositions() {
		return positions;
	}
	
	public void addPosition(TraineeshipPosition position) {
		this.positions.add(position);
		
	}
	
	public void deletePositon(TraineeshipPosition position) {
		this.positions.remove(position);
	}

}
