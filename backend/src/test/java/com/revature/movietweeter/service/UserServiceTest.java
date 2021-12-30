package com.revature.movietweeter.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import com.revature.movietweeter.dao.UserDAO;
import com.revature.movietweeter.exception.InvalidLoginException;
import com.revature.movietweeter.model.User;

public class UserServiceTest {

	private UserService sut;

	@Test
	public void testGetUserByUsernameAndPassword() throws InvalidLoginException {

		UserDAO mockUserDao = mock(UserDAO.class);

		String unhashedPassword = "password";
		String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(8));

		User u = new User("user", hashedPassword);

		when(mockUserDao.getUserfromUsername(eq("user"))).thenReturn(u);
		// when(mockUserDao.getUserByUserNameAndPassword(eq("user"),
		// eq(hashedPassword))).thenReturn(u);

		UserService us = new UserService(mockUserDao);

		User actual = us.getUserByUsernameAndPassword("user", unhashedPassword);

		User expected = new User("user", hashedPassword);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	public void testGetUserByUsernameAndPasswordUsernameNotFound() {
		UserDAO mockUserDao = mock(UserDAO.class);

		when(mockUserDao.getUserfromUsername(eq("user"))).thenThrow(new DataAccessException("user not found") {
		});

		UserService us = new UserService(mockUserDao);

		Assertions.assertThrows(InvalidLoginException.class, () -> {
			us.getUserByUsernameAndPassword("user", "password");
		});

	}

	@Test
	void testGetUserByUsernameAndPasswordIncorrectPassword() {
		UserDAO mockUserDao = mock(UserDAO.class);

		String unhashedPassword = "password";
		String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(8));

		when(mockUserDao.getUserfromUsername(eq("user"))).thenReturn(new User("user", hashedPassword));

		UserService us = new UserService(mockUserDao);

		Assertions.assertThrows(InvalidLoginException.class, () -> {
			us.getUserByUsernameAndPassword("user", "wrongPassword");
		});

	}

	@Test
	void testCreateUser() throws InvalidLoginException {
		UserDAO mockUserDao = mock(UserDAO.class);

		String unhashedPassword = "password";
		String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(8));

		when(mockUserDao.addUser(eq("user"), anyString())).thenReturn(new User("user", hashedPassword));

		UserService us = new UserService(mockUserDao);

		User actual = us.createUser("user", unhashedPassword);

		User expected = new User("user", hashedPassword);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testCreateUserUsernameNull() {
		UserDAO mockUserDao = mock(UserDAO.class);

		UserService us = new UserService(mockUserDao);

		Assertions.assertThrows(InvalidLoginException.class, () -> {
			us.createUser(null, "password");
		});
	}

	@Test
	void testCreateUserPasswordNull() {
		UserDAO mockUserDao = mock(UserDAO.class);

		UserService us = new UserService(mockUserDao);

		Assertions.assertThrows(InvalidLoginException.class, () -> {
			us.createUser("user", null);
		});
	}

	@Test
	void testCreateUserButUserAlreadyTaken() {
		UserDAO mockUserDao = mock(UserDAO.class);
		
		when(mockUserDao.addUser(eq("user"), anyString())).thenThrow(new DataAccessException("message") {
			
		});
		UserService us = new UserService(mockUserDao);
		
		Assertions.assertThrows(InvalidLoginException.class, () -> {
			us.createUser("user","password");
		});
	}
}
