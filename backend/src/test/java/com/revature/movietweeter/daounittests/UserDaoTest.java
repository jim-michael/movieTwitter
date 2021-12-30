package com.revature.movietweeter.daounittests;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

import com.revature.movietweeter.dao.UserDAO;
import com.revature.movietweeter.model.User;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDaoTest {
	@Autowired
	private EntityManager em;

	@Autowired
	private UserDAO sut;

	@Test
	@Transactional
	public void testGetUserByUsernameAndPassword_positive() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		User actual = this.sut.getUserByUserNameAndPassword("connor_huston", "pass12345");

		User expected = new User("connor_huston", "pass12345");
		expected.setId(1);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testGetUserByUsernameAndPassword_incorrectUsername() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.sut.getUserByUserNameAndPassword("wrongusername", "pass12345");
		});
	}

	@Test
	@Transactional
	public void testGetUserByUsernameAndPassword_incorrectPassword() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.sut.getUserByUserNameAndPassword("connor_huston", "wrongpassword");
		});
	}

	@Test
	@Transactional

	public void testGetUserByUsernameAndPassword_incorrectUsernameAndPassword() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.sut.getUserByUserNameAndPassword("wrongusername", "wrongpassword");
		});
	}

	@Test
	@Transactional
	public void testGetUserByUsername() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		User actual = this.sut.getUserfromUsername("connor_huston");

		User expected = new User("connor_huston", "pass12345");
		expected.setId(1);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	@Transactional
	public void testGetUserByUsername_incorrectUserName() {

		User user = new User("connor_huston", "pass12345");
		this.em.persist(user);

		this.em.flush();

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.sut.getUserfromUsername("wrongusername");
		});

	}
	
	@Test
    @Transactional

    public void testAddUser() {

        User addedUser = this.sut.addUser("leon", "67890word");

        User expected = new User("leon", "67890word");
        expected.setId(1);

        Assertions.assertEquals(expected, addedUser);
    }
	
	@Test
    @Transactional

    public void testAddUserButUsernameAlreadyExists() {

        User addedUser = this.sut.addUser("leon", "67890word");

        Assertions.assertThrows(DataAccessException.class, () -> {
        	this.sut.addUser("leon", "khgksjhg");
        });
    }
	

}

