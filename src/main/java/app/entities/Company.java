package app.entities;

import jakarta.persistence.*;
import java.util.*;


@Entity
@Table(name="company")
public class Company{

	// Fields
	
	@Id
    @Column(name = "username")
    private String username;  
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="location")
	private String companyLocation;
	
	@OneToMany(cascade=CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "company")
	private List<TraineeshipPosition> positions = new ArrayList<>();
	
	
	// Constructors
	
	public Company(){
	}
	
	public Company(String username, String name, String location, List<TraineeshipPosition> positions) {
		this.username = username;
		this.companyName=name;
		this.companyLocation=location;
		this.positions = positions;
	}


	// Getters/Setters
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	public List<TraineeshipPosition> getPositions() {
		return positions;
	}

	public void setPositions(List<TraineeshipPosition> positions) {
		this.positions = positions;
	}
	
	
	public void addPosition(TraineeshipPosition position) {
		this.positions.add(position);
		
	}
	
	public void deletePositon(TraineeshipPosition position) {
		if (this.positions.contains(position)) {
			this.positions.remove(position);
		}
		
	}
	
	public String toString() {
		return "Company [name:" + companyName + ", location" + companyLocation + "]"; 
	}
	
	public List<TraineeshipPosition> getAssignedPositions() {
		List<TraineeshipPosition> assigned = new ArrayList<>();
		
		for(int i=0; i<positions.size(); i++) {
			if(positions.get(i).isAssigned()==true) {   
				assigned.add(positions.get(i));	
			}
			
		}
		return assigned;
	}
	
}
