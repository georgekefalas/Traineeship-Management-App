package app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import app.entities.*;
import app.services.*;


import java.util.*;


@Configuration
public class CustomSecuritySuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
    private ProfessorService professorService;
	
	@Autowired
    private CommitteeMemberService comService;
	
	@Autowired
    private StudentService studService;
	
	@Autowired
    private CompanyService compService;
	
	 @Override
	    protected void handle(
	    		HttpServletRequest request, 
	    		HttpServletResponse response, 
	    		Authentication authentication)
	    throws java.io.IOException {
	       
		 	String targetUrl = determineTargetUrl(authentication);
	        
		 	if(response.isCommitted()) return;
	        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	        redirectStrategy.sendRedirect(request, response, targetUrl);
	    }

	    protected String determineTargetUrl(Authentication authentication){
	    	String username = authentication.getName();	    	
	    	String url = "/login?error=true";
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        List<String> roles = new ArrayList<String>();
	        
	        for(GrantedAuthority a : authorities){
	            roles.add(a.getAuthority());
	        }
	        
	        if(roles.contains("STUDENT")){
	        	Optional<Student> stud= studService.findByUsername(username);
	            if (stud.isPresent()) {url = "/student/dashboard";}	    
	        	else {url =  "/student/create_form";} 
	        }else if(roles.contains("PROFESSOR")) {
	        	Optional<Professor> prof= professorService.findByUsername(username);
	        	if (prof.isPresent()) {url = "/professor/dashboard";}	    
	        	else {url =  "/professor/create_form";}              
	        }else if(roles.contains("COMPANY")) {
	        	Optional<Company> comp= compService.findByUsername(username);
	        	if (comp.isPresent()) {url = "/company/dashboard";}	    
	        	else {url =  "/company/create_form";}
	        }else if(roles.contains("COMMITTEEMEMBER")) {
	        	Optional<CommitteeMember> mem= comService.findByUsername(username);
	        	if (mem.isPresent()) {url = "/committee/dashboard";}	    
	        	else {url =  "/committee/create_form";}  
	        }
	        
	        return url;
	    }
}
