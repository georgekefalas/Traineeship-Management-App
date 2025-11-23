package app.controllers;

import app.entities.*;
import app.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	
	@RequestMapping("/student/dashboard")
	public String showDashboard(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String studUsername = authentication.getName();
	    Student student = studentService.retrieveProfile(studUsername);
		
	    model.addAttribute("lookStatus", student.getLookingForTraineeship());	    
	    model.addAttribute("assignStatus", student.getAssignedStatus());    
		
	    return "student/dashboard";
	}
	
	
	@RequestMapping("/student/create")
    public String createProfile(@ModelAttribute("student") Student student) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
       
        student.setUsername(username);       
    	studentService.createProfile(student);
        
    	return "redirect:/student/dashboard";  
	}
	
	
	@RequestMapping("student/create_form")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student()); 
        
        return "student/create_form";
    }
	
	
	@RequestMapping("student/profile")
    public String retrieveProfile(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
        String studUsername = authentication.getName();
        
        Student stud = studentService.retrieveProfile(studUsername);    	
    	model.addAttribute("student", stud);	
    	
    	return "student/profile";
    }
	
	
	@RequestMapping("student/apply")
	public String applyForTraineeship(Model model) {
	   
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String studUsername = authentication.getName();
	    Student student = studentService.retrieveProfile(studUsername);
	    
	    studentService.applyForTraineeship(studUsername);
	    model.addAttribute("status", student.getLookingForTraineeship()); 
	    
	    return "student/apply";
	}
	
	
	@RequestMapping("student/logbook")
	public String showLogbook(Model model) {
	    
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
	    Student student = studentService.retrieveProfile(username);
	    
	    TraineeshipPosition tp = student.getAssignedTraineeship();

	    model.addAttribute("traineeship", tp);
	    return "student/logbook";
	}
	
	
	@RequestMapping("student/fill-logbook/{traineeshipId}")
	public String fillLogbook(@PathVariable("traineeshipId") Integer traineeshipId,
	                          @ModelAttribute("logbookContent") String logbookContent) {

	    studentService.fillLogbook(traineeshipId, logbookContent);
	    return "redirect:/student/dashboard";
	}

}