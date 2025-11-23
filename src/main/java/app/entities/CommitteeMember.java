package app.entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "committee_member")
public class CommitteeMember {
	
	// Fields
	
	@Id
    @Column(name = "username")
    private String username;
	
	@Column(name="member_name")
	private String memberName;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "committee_applicant_students",
	    joinColumns = @JoinColumn(name = "committee_member_id"),
	    inverseJoinColumns = @JoinColumn(name = "student_id")
	)
	private List<Student> applicantStudents = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "committee_positions",
	    joinColumns = @JoinColumn(name = "committee_member_id"),
	    inverseJoinColumns = @JoinColumn(name = "position_id")
	)
	private List<TraineeshipPosition> positions = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "committee_available_professors",
	    joinColumns = @JoinColumn(name = "committee_member_id"),
	    inverseJoinColumns = @JoinColumn(name = "professor_id")
	)
	private List<Professor> availableProfessors = new ArrayList<>();

	
	// Constructors
	
	public CommitteeMember() {
	}
	
	public CommitteeMember(String username, String memberName, List<Student> applicants,List<TraineeshipPosition> positions, List<Professor> availableProfessors) {
		this.username = username;
		this.memberName = memberName;
		this.applicantStudents = applicants;
		this.positions = positions;
		this.availableProfessors = availableProfessors;
	}
	
	// Getters/Setters
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getMemberName() {
		return memberName;
	}
	
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
    public List<Student> getApplicantStudents() {
        return applicantStudents;
    }

    public void setApplicantStudents(ArrayList<Student> applicantStudents) {
        this.applicantStudents = applicantStudents;
    }

    public List<TraineeshipPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<TraineeshipPosition> positions) {
        this.positions = positions;
    }

    public List<Professor> getAvailableProfessors() {
        return availableProfessors;
    }

    public void setAvailableProfessors(List<Professor> availableProfessors) {
        this.availableProfessors = availableProfessors;
    }

    @Override
    public String toString() {
        return "CommitteeMember{" +
                "username='" +
                ", applicantStudents=" + applicantStudents +
                ", positions=" + positions +
                ", availableProfessors=" + availableProfessors +
                '}';
    }
	
}
