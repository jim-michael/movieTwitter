package com.revature.movietweeter.integrationtests;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.movietweeter.dto.SignUpDTO;
import com.revature.movietweeter.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationControllerTest {
	@Autowired
	private MockMvc mvc;

	@Autowired
	private EntityManagerFactory emf;

	@Autowired
	private ObjectMapper mapper;

	@BeforeEach
	public void setUp() {

		EntityManager em = emf.createEntityManager();
		Session session = em.unwrap(Session.class);
		Transaction tx = session.beginTransaction();

		String pw_hash = BCrypt.hashpw("pass12345", BCrypt.gensalt(8));

		User mike = new User("mike", pw_hash);
		session.persist(mike);

		tx.commit();

		session.close();
	}

	@Test
	public void testSignUp() throws Exception {
		EntityManager em = emf.createEntityManager();

		SignUpDTO dto = new SignUpDTO("connor_huston", "pass12345");
		String jsonToSend = mapper.writeValueAsString(dto);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(201));

	}

	@Test
	public void testSignUpUserAlreadyExists() throws Exception {
		SignUpDTO dto = new SignUpDTO("mike", "pass12345");
		String jsonToSend = mapper.writeValueAsString(dto);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string("Username already taken"));

	}

	@Test
	public void testSignUpUserBlankUsername() throws Exception {
		SignUpDTO dto = new SignUpDTO(null, "pass12345");
		String jsonToSend = mapper.writeValueAsString(dto);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string("Username and/or password cannot be blank"));

	}

	@Test
	public void testSignUpUserBlankPassword() throws Exception {
		SignUpDTO dto = new SignUpDTO("leon", null);
		String jsonToSend = mapper.writeValueAsString(dto);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/signup").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string("Username and/or password cannot be blank"));

	}

	@Test
	public void testLogin() throws Exception {
		EntityManager em = emf.createEntityManager();
		SignUpDTO dto = new SignUpDTO("mike", "pass12345");

		String jsonToSend = mapper.writeValueAsString(dto);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		User expectedObject = new User("mike", em.find(User.class, 1).getPassword());
		expectedObject.setId(1);

		String expectedJson = mapper.writeValueAsString(expectedObject);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJson));

	}

	@Test
	public void testLoginUsernameNotFound() throws Exception {
		SignUpDTO dto = new SignUpDTO("leon", "pass12345");
		String jsonToSend = mapper.writeValueAsString(dto);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string("Username and/or password is incorrect"));
	}

	@Test
	public void testLoginIncorrectPassword() throws Exception {
		SignUpDTO dto = new SignUpDTO("mike", "wrongpass");
		String jsonToSend = mapper.writeValueAsString(dto);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().string("Username and/or password is incorrect"));
	}

	@Test
	public void testCheckLoginStatusLoggedIn() throws Exception {
		User fakeUser = new User("user", "password");
		fakeUser.setId(2);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", fakeUser);

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/loginstatus").session(session);

		String expectedJsonUser = mapper.writeValueAsString(fakeUser);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJsonUser));
	}

	@Test
	public void testCheckLoginStatusNotLoggedIn() throws Exception {
		User fakeUser = new User();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/loginstatus");

		String expectedJsonUser = mapper.writeValueAsString(fakeUser);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJsonUser));
	}

	@Test
	public void testLogOut() throws Exception {
		User fakeUser = new User();

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/logout");

		String expectedJsonUser = mapper.writeValueAsString(fakeUser);

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJsonUser));
	}
}
