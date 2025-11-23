package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entities.*;
import app.services.UserService;


@Controller
public class AuthController {
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping("/login")
    public String login(){return "auth/login";}
       
	
    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());        
        return "auth/register";
    }
    

    @PostMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model){
       
        if(userService.isUserPresent(user)){
            model.addAttribute("successMessage", "User already registered!");
           
            return "auth/login";
        }

        userService.createUser(user);           
        return "auth/login";
    }
	
}
