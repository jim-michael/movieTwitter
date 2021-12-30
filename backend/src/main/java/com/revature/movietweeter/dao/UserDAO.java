package com.revature.movietweeter.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.revature.movietweeter.model.User;

@Repository
public class UserDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public User getUserfromUsername(String username) {
		User user = em.createQuery("FROM User u WHERE u.username = :username", User.class)
				.setParameter("username", username)
				.getSingleResult();
		
		return user;
	}
	
	@Transactional
	public User getUserByUserNameAndPassword(String username, String pw_hash) {
		User user = em.createQuery("FROM User u WHERE u.username = :username and u.password = :pw_hash", User.class)
				.setParameter("username", username)
				.setParameter("pw_hash", pw_hash)
				.getSingleResult();
		
		return user;
	}
	
	@Transactional
	public User addUser(String username, String pw_hash) {
		User createdUser = new User(username, pw_hash);
		
		em.persist(createdUser);
		
		return createdUser;
	}
	
}
