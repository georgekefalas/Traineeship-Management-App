package app.entities;

import java.util.*;

import jakarta.persistence.*;


@Entity
@Table(name = "student")
public class Student {

	// Fields
	
	@Id
    @Column(name = "username")
    private String username; 
	
    @Column(name = "student_name")
    private String studentName;

    @Column(name = "AM")
    private String AM;

    @Column(name = "avg_grade")
    private double avgGrade;

    @Column(name = "preferred_location")
    private String preferredLocation;

    @ElementCollection
    @CollectionTable(name = "student_interests", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();
    
    @Column(name = "looking_for_traineeship")
    private boolean lookingForTraineeship;

    @OneToOne(cascade=CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "student")
    private TraineeshipPosition assignedTraineeship;

    
    // Constructors
    
    public Student() {
    }
    
    public Student(String username, String studentName, String AM, double avgGrade, String preferredLocation, List<String> interests, List<String> skills, boolean lookingForTraineeship, TraineeshipPosition assignedTraineeship) {
        this.username = username;
        this.studentName = studentName;
        this.AM = AM;
        this.avgGrade = avgGrade;
        this.preferredLocation = preferredLocation;
        this.interests = interests;
        this.skills = skills;
        this.lookingForTraineeship = lookingForTraineeship;
        this.assignedTraineeship = assignedTraineeship;
    }
    
    
    // Getters/Setters
    
    public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getAM() {
        return AM;
    }

    public void setAM(String AM) {
        this.AM = AM;
    }

    public double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
   
    public boolean getLookingForTraineeship() {
        return lookingForTraineeship;
    }

    public void setLookingForTraineeship(boolean lookingForTraineeship) {
        this.lookingForTraineeship = lookingForTraineeship;
    }

    public TraineeshipPosition getAssignedTraineeship() {
        return assignedTraineeship;
    }

    public void setAssignedTraineeship(TraineeshipPosition assignedTraineeship) {
        this.assignedTraineeship = assignedTraineeship;
    }
    
    public boolean getAssignedStatus() {
    	boolean assignStatus;
	    
    	if (this.assignedTraineeship != null) {assignStatus = true;}
	    else {assignStatus = false;}
    	
    	return assignStatus;
    }

    @Override
    public String toString() {
        return "Student{" + '\'' +
            ", studentName='" + studentName + '\'' +
            ", AM='" + AM + '\'' +
            ", avgGrade=" + avgGrade +
            ", preferredLocation='" + preferredLocation + '\'' +
            ", interests='" + interests + '\'' +
            ", skills='" + skills + '\'' +
            ", lookingForTraineeship=" + lookingForTraineeship +
            '}';
    }

}