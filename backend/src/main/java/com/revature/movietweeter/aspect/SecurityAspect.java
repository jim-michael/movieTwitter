package com.revature.movietweeter.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.revature.movietweeter.model.User;

@Aspect
@Component
public class SecurityAspect {

	@Autowired
	private HttpServletRequest req;

	@Around("@annotation(com.revature.movietweeter.annotation.AuthorizedUser)")
	public Object protectEndpointUsersOnly(ProceedingJoinPoint pjp) throws Throwable {

		HttpSession session = req.getSession();

		User currentlyLoggedInUser = (User) session.getAttribute("currentUser");
		
		if (currentlyLoggedInUser == null) {
			return ResponseEntity.status(401).body("You are not logged in");
		}
		
		Object returnValue = pjp.proceed();
		return returnValue;
	}
}
