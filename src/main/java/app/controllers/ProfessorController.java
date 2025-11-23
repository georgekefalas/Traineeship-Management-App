package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entities.*;
import app.services.ProfessorService;

import java.util.*;

@Controller
public class ProfessorController {

    
	@Autowired
    private ProfessorService professorService;
	
	
	@RequestMapping("professor/dashboard")
    public String showDashboard(Model model) {
        return "professor/dashboard"; 
    }

	
    @RequestMapping("professor/create")
    public String createProfile(@ModelAttribute("professor") Professor professor) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        professor.setUsername(username);        
    	professorService.createProfile(professor);
    	
        return "redirect:/professor/dashboard";  
    }
    
    
    @RequestMapping("professor/create_form")
    public String showCreateForm(Model model) {
        model.addAttribute("professor", new Professor()); 
        
        return "professor/create_form";
    }

    
    @RequestMapping("professor/profile")
    public String retrieveProfile(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
        String profUsername = authentication.getName();
        
        Professor prof = professorService.retrieveProfile(profUsername);    	
    	model.addAttribute("professor", prof);	
    	
    	return "professor/profile";
    }

  
    @RequestMapping("professor/positions")
    public String showSupervisingPositions(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String profUsername = authentication.getName();
		
		List<TraineeshipPosition> positions = professorService.showSupervisingPositions(profUsername);		
		model.addAttribute("positions", positions);
				    	
    	return "professor/positions";
    }
    

    @GetMapping("professor/evaluate_form/{positionId}")
    public String showEvaluationForm(@PathVariable("positionId") Integer positionId, Model model) {
        
    	Evaluation evaluation = new Evaluation();  
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("positionId", positionId);
        
        return "professor/evaluate_form";  
    }
    
    
    @PostMapping("professor/evaluate/{positionId}")
    public String evaluateTraineeship(@PathVariable("positionId") Integer positionId, @ModelAttribute("evaluation") Evaluation evaluation) {
        
    	evaluation.setType(true); 
    	professorService.evaluateTraineeship(positionId, evaluation);
    	
        return "professor/dashboard"; 
    }
    
    
    @GetMapping("professor/guidelines")
    public String showGuidelines() {
        return "professor/guidelines";
    }
    
}
