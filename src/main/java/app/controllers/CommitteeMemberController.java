package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entities.*;
import app.services.CommitteeMemberService;

import java.util.*;

@Controller
public class CommitteeMemberController {
	
	
	@Autowired
    private CommitteeMemberService comService;
	
	
	@RequestMapping("committee/dashboard")
    public String showDashboard(Model model) {
        return "committee/dashboard"; 
    }

	
    @RequestMapping("committee/create")
    public String createProfile(@ModelAttribute("committeeMember") CommitteeMember member) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        member.setUsername(username);       
    	comService.createProfile(member);
    	
        return "redirect:/committee/dashboard";  
    }
    
    
    @RequestMapping("committee/create_form")
    public String showCreateForm(Model model) {
        model.addAttribute("committeeMember", new CommitteeMember()); 
        
        return "committee/create_form";
    }

    
    @RequestMapping("committee/profile")
    public String retrieveProfile(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
        String memberUsername = authentication.getName();
        
        CommitteeMember member = comService.retrieveProfile(memberUsername);    	
    	model.addAttribute("committeeMember", member);	
    	
    	return "committee/profile";
    }
    
    
    @RequestMapping("committee/applicants")
    public String showApplicants(Model model) {
        
    	List<Student> applicants = comService.showApplicants();
        model.addAttribute("students", applicants);
        
        return "committee/applicants";
    }
    
    
    @RequestMapping("committee/matching-traineeships/{username}")
    public String showMatchingTraineeships(
            @PathVariable("username") String username,
            @RequestParam(name = "strategy", defaultValue = "skillBasedStrategy") String strategy,
            Model model) {
        
    	List<TraineeshipPosition> matches = comService.showMathingTraineeships(username, strategy);
        model.addAttribute("positions", matches);
        model.addAttribute("studentUsername", username);
        model.addAttribute("selectedStrategy", strategy);
        
        return "committee/matching_traineeships";
    }
    
    
    @RequestMapping("committee/assign-traineeship")
    public String assignTraineeship(@RequestParam Integer positionId,
                                    @RequestParam String studentUsername) {
        comService.assignTraineeship(positionId, studentUsername);
        
        return "redirect:/committee/applicants";
    }
    
    
    @RequestMapping("committee/in-progress")
    public String showInProgress(Model model) {
        
    	List<TraineeshipPosition> inProgress = comService.showTraineeshipsInProgress();
        model.addAttribute("positions", inProgress);
        
        return "committee/in_progress";
    }
    
    
    @RequestMapping("committee/available-professors")
    public String showAvailableProfessors(@RequestParam Integer positionId, 
    		@RequestParam(name = "strategy", defaultValue = "lowestWorkloadStrategy") String strategy,
    		Model model) {
        
    	List<Professor> professors = comService.showAvailableProfessors(strategy, positionId);
        model.addAttribute("professors", professors);
        model.addAttribute("positionId", positionId);
        model.addAttribute("selectedStrategy", strategy);
        
        return "committee/available_professors";
    }
    
    
    @RequestMapping("committee/assign-supervisor")
    public String assignSupervisor(@RequestParam Integer positionId,
                                   @RequestParam String professorUsername) {
        comService.assignSupervisingProfessor(positionId, professorUsername);
        
        return "redirect:/committee/in-progress";
    }
    
    
    @RequestMapping("committee/monitor/{positionId}")
    public String monitorTraineeship(@PathVariable Integer positionId, Model model) {
        
    	List<Evaluation> evaluations = comService.monitorTraineeship(positionId);
        model.addAttribute("evaluations", evaluations);
        model.addAttribute("positionId", positionId);
        
        return "committee/monitor";
    }
    
    
    @RequestMapping("committee/terminate/{positionId}")
    public String terminateTraineeship(@PathVariable Integer positionId,
                                       @RequestParam("result") String result) {
        
    	boolean passed = result.equalsIgnoreCase("pass");
        comService.terminateTraineeship(positionId, passed);
        
        return "redirect:/committee/in-progress";
    }
    
}
