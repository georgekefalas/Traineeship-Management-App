package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.entities.*;
import app.mappers.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	
	@Autowired 
	private UserMapper userMapper;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	// Constructors
	public UserServiceImpl() {
		
	}
	
	@Autowired
	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	
	// Methods
	
	@Override
	public void createUser(User user) {
		
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userMapper.save(user);
	}
	
	
	@Override
	public boolean isUserPresent(User user) {
		Optional<User> storedUser = userMapper.findById(user.getUsername());
		return storedUser.isPresent();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    return userMapper.findById(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
	}
}
