package app.controllers;

import app.entities.*;
import app.services.CompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class CompanyController {
	
	
	@Autowired
	private CompanyService companyService;

	
	@RequestMapping("company/dashboard")
	public String showDashboard() {
		return "company/dashboard";
	}
	
	
	@RequestMapping("company/create")
	public String createProfile(@ModelAttribute("company") Company company) {
	    
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        company.setUsername(username);
		companyService.createProfile(company);
	    
	    return "redirect:/company/dashboard";  
	 }
	
	
	@RequestMapping("company/create_form")
    public String showCreateForm(Model model) {
        model.addAttribute("company", new Company()); 
        
        return "company/create_form";
    }
	
	
	@RequestMapping("company/profile")
	public String retrieveProfile(Model model) {
	   
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();	
	  String compUsername = authentication.getName();
	        
	  Company comp= companyService.retrieveProfile(compUsername);    	
	  model.addAttribute("company", comp);	
	    	
	  return "company/profile";
	}
	 
	 
	@GetMapping("company/addposition")
	public String showAddPositionForm(Model model) {
		model.addAttribute("traineeshipPosition", new TraineeshipPosition());
		 
		return "company/pos_form";
	}
	 
	 
	@RequestMapping("company/addposition")
	public String addPosition(@ModelAttribute("traineeshipPosition") TraineeshipPosition position ) {
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String compUserName = authentication.getName();
			
		companyService.addPosition(compUserName, position);
			
		return "redirect:/company/dashboard"; 
			
	}
	
	@RequestMapping("company/deleteposition/{positionId}")
	public String deletePosition(@PathVariable("positionId") Integer positionId) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String compUserName = authentication.getName();
		
		companyService.deletePosition(compUserName, positionId);
		
		return "redirect:/company/dashboard"; 
		
	}
	 
	
	@RequestMapping("company/availablepositions")
	public String showAvailablePositions(Model model) {
		
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  String compUserName = authentication.getName();
	  
	  List<TraineeshipPosition> positions = companyService.showAvailablePositions(compUserName);
	  model.addAttribute("positions", positions);
	  
	  return "company/availablepositions";
	}
	
	
	@RequestMapping("company/detail_pos/{positionId}")
	public String showDetailsPos(@PathVariable("positionId") Integer positionId, Model model) {
		
		TraineeshipPosition pos=companyService.showDetails(positionId);
	
        model.addAttribute("position", pos);
        
        return "company/detail_pos"; 	
	}
	
	
	@RequestMapping("company/assignedpositions")
	public String showAssignedPositions(Model model) {
	  
	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	  String compUserName = authentication.getName();
		  
	  List<TraineeshipPosition> positions = companyService.showAssignedPositions(compUserName);
	  model.addAttribute("positions", positions);
		
	  return "company/assignedpositions";
		
	}
	
	
    @RequestMapping("company/evaluate_form/{positionId}")
    public String showEvaluationForm(@PathVariable("positionId") Integer positionId, Model model) {
        
    	Evaluation evaluation = new Evaluation();  
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("positionId", positionId);
        
        return "company/evaluate_form";  
    }
	
	
    @RequestMapping("company/evaluate/{positionId}")
    public String evaluateTraineeship(@PathVariable("positionId") Integer positionId, @ModelAttribute("evaluation") Evaluation evaluation) {
        
    	evaluation.setType(false); 
    	companyService.evaluateTraineeship(positionId, evaluation);
        return "company/dashboard"; 
    }
    
}
