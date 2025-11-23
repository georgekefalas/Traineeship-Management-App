package app.entities;

public enum Role {
    STUDENT("Student"),
    PROFESSOR("Professor"),
    COMPANY("Company"),
    COMMITTEEMEMBER("CommitteeMember");
    
    private final String value;
    
    private Role(String value) {
    	this.value = value;
    }
    
    public String getValue() {
    	return value;
    }
}