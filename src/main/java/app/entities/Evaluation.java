package app.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluation")
public class Evaluation {
	
	// Fields
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "type")
    private boolean type;

    @Column(name = "motivation")
    private int motivation;

    @Column(name = "efficiency")
    private int efficiency;

    @Column(name = "effectiveness")
    private int effectiveness;
    
    
    // Constructors

    public Evaluation() {
    }
    
    public Evaluation(Integer id, boolean type, int motivation, int efficiency, int effectiveness) {
        this.id = id;
        this.type = type;
        this.motivation = motivation;
        this.efficiency = efficiency;
        this.effectiveness = effectiveness;
    }
    
    
    // Getters/Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public boolean getType() {
    	return type;
    }
    
    public void setType(boolean type) {
    	this.type = type;
    }

    public int getMotivation() {
        return motivation;
    }

    public void setMotivation(int motivation) {
        this.motivation = motivation;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }

    public int getEffectiveness() {
        return effectiveness;
    }

    public void setEffectiveness(int effectiveness) {
        this.effectiveness = effectiveness;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
            "id=" + id +
            ", motivation=" + motivation +
            ", efficiency=" + efficiency +
            ", effectiveness=" + effectiveness +
            '}';
    }
}