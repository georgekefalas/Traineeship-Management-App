package app.services;


import app.entities.*;


public interface UserService {
	
	public void createUser(User user);
	
	public boolean isUserPresent(User user);
	
}
