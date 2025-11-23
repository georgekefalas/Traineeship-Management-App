package app.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;



@Entity
@Table(name = "traineeship_position")
public class TraineeshipPosition {

	//Fields
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @ElementCollection
    @CollectionTable(name = "traineeship_topics", joinColumns = @JoinColumn(name = "traineeship_id"))
    @Column(name = "topic")
    private List<String> topics = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "traineeship_skills", joinColumns = @JoinColumn(name = "traineeship_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Column(name = "assigned")
    private boolean isAssigned;

    @Column(name = "student_logbook")
    private String studentLogbook;

    @Column(name = "pass_fail_grade")
    private boolean passFailGrade;

    @OneToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "supervisor_id")
    private Professor supervisor;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "traineeship_id")    
    private List<Evaluation> evaluations = new ArrayList<>();

    
    // Constructors
    
    public TraineeshipPosition() {
    	
    }
    
    public TraineeshipPosition(Integer id, String title, String description, LocalDate fromDate, LocalDate toDate, List<String> topics, List<String> skills, boolean assigned, String studentLogbook, boolean passFailGrade, Student student, Professor supervisor, Company company, List<Evaluation> evaluations) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.topics = topics;
        this.skills = skills;
        this.isAssigned = assigned;
        this.studentLogbook = studentLogbook;
        this.passFailGrade = passFailGrade;
        this.student = student;
        this.supervisor = supervisor;
        this.company = company;
        this.evaluations = evaluations;
    }
    
    
    // Getters/Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean is_assigned) {
        this.isAssigned = is_assigned;
    }

    public String getStudentLogbook() {
        return studentLogbook;
    }

    public void setStudentLogbook(String studentLogbook) {
        this.studentLogbook = studentLogbook;
    }

    public boolean isPassFailGrade() {
        return passFailGrade;
    }

    public void setPassFailGrade(boolean passFailGrade) {
        this.passFailGrade = passFailGrade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Professor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Professor supervisor) {
        this.supervisor = supervisor;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    
    public boolean isProfessorEvaluationSubmitted() {
        return evaluations != null && evaluations.stream().anyMatch(Evaluation::getType);
    }
    
    public boolean isCompanyEvaluationSubmitted() {
        return evaluations != null && evaluations.stream().anyMatch(e -> !e.getType());
    }

    @Override
    public String toString() {
        return "TraineeshipPosition{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", fromDate=" + fromDate +
            ", toDate=" + toDate +
            ", topics='" + topics + '\'' +
            ", skills='" + skills + '\'' +
            ", assigned=" + isAssigned +
            ", studentLogbook=" + studentLogbook +
            ", passFailGrade=" + passFailGrade +
            '}';
    }

}