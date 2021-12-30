package com.revature.movietweeter.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietweeter.dto.SignUpDTO;
import com.revature.movietweeter.exception.InvalidLoginException;
import com.revature.movietweeter.model.User;
import com.revature.movietweeter.service.UserService;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserService us;
	
	@Autowired
	private HttpServletRequest req;
	
	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@PostMapping(path = "/signup")
	public ResponseEntity<Object> signUp(@RequestBody SignUpDTO dto) {
		try {
			User user = this.us.createUser(dto.getUsername(), dto.getPassword());
			
			HttpSession session = req.getSession();
			session.setAttribute("currentUser", user);
			
			return ResponseEntity.status(201).body(user);
			
		} catch (InvalidLoginException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@PostMapping(path = "/login")
	public ResponseEntity<Object> login(@RequestBody SignUpDTO dto) {
		try {
			User user = this.us.getUserByUsernameAndPassword(dto.getUsername(), dto.getPassword());
			
			HttpSession session = req.getSession();
			session.setAttribute("currentUser", user);
			
			return ResponseEntity.status(200).body(user);
		} catch (InvalidLoginException e){
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@PostMapping(path = "/logout")
	public ResponseEntity<User> logout() {
		req.getSession().invalidate();
		
		return ResponseEntity.status(200).body(new User());
	}
	
	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@GetMapping(path = "/loginstatus")
	public ResponseEntity<Object> checkLoginStatus() {
		User currentlyLoggedInUser = (User) req.getSession().getAttribute("currentUser");
		
		
		if (currentlyLoggedInUser != null) {
			return ResponseEntity.status(200).body(currentlyLoggedInUser);
		}
		
		return ResponseEntity.status(200).body(new User());
	}
	
}
