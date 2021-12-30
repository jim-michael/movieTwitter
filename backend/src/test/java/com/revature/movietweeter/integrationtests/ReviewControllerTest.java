package com.revature.movietweeter.integrationtests;

import java.util.Date;
import java.util.List;

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
import com.revature.movietweeter.dto.AddReviewDTO;
import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewControllerTest {
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

		User connor = new User("connor", pw_hash);
		session.persist(connor);

		Date d = new Date();

		Review r1 = new Review("title1", "text1", 5, d, "tt1", mike);
		Review r2 = new Review("title2", "text2", 5, d, "tt2", mike);

		Review r3 = new Review("title3", "text3", 5, d, "tt3", connor);
		Review r4 = new Review("title4", "text4", 5, d, "tt4", connor);

		session.persist(r1);
		session.persist(r2);
		session.persist(r3);
		session.persist(r4);

		tx.commit();

		session.close();
	}

	@Test
	public void testGetAllReviews() throws Exception {
		EntityManager em = emf.createEntityManager();

		List<Review> expected = em.createQuery("FROM Review r ORDER BY r.submissionTime DESC", Review.class)
				.getResultList();
		String expectedJson = mapper.writeValueAsString(expected);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/reviews");

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJson));
	}

	@Test
	public void testGetAllReviewsByAuthor() throws Exception {
		EntityManager em = emf.createEntityManager();

		List<Review> expected = em.createQuery("FROM Review r WHERE r.author = 2", Review.class).getResultList();

		String expectedJson = mapper.writeValueAsString(expected);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/users/2/reviews");

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJson));
	}
	
	@Test
	public void testGetAllReviewsByMovie() throws Exception {
		EntityManager em = emf.createEntityManager();

		List<Review> expected = em.createQuery("FROM Review r WHERE r.movieApiId = 'tt3'", Review.class).getResultList();

		String expectedJson = mapper.writeValueAsString(expected);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/movies/tt3/reviews");

		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(200))
				.andExpect(MockMvcResultMatchers.content().json(expectedJson));
	}
	
	@Test
	public void testCreateReview() throws Exception {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, 1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", user);
		
		AddReviewDTO dto = new AddReviewDTO("title", "text", "tt0126029", "5");
		String jsonToSend = mapper.writeValueAsString(dto);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reviews").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON).session(session);
		
		Review expectedObject = new Review("title", "text", 5, new Date(), "tt0126029",user);

		String expectedJson = mapper.writeValueAsString(expectedObject);
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(201));
		
	}
	
	@Test
	public void testCreateReviewInvalidRating() throws Exception {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, 1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", user);
		
		AddReviewDTO dto = new AddReviewDTO("title", "text", "tt0126029", "sdfdsfg");
		String jsonToSend = mapper.writeValueAsString(dto);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reviews").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON).session(session);
		
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("Rating must be integer 1-5"));
		
	}
	
	@Test
	public void testCreateReviewInvalidMovie() throws Exception {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, 1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", user);
		
		AddReviewDTO dto = new AddReviewDTO("title", "text", "wrongId", "sdfdsfg");
		String jsonToSend = mapper.writeValueAsString(dto);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reviews").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON).session(session);
		
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("movie with imdbId does not exist"));
		
	}
	
	@Test
	public void testCreateReviewBlankTitle() throws Exception {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, 1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", user);
		
		AddReviewDTO dto = new AddReviewDTO(null, "text", "tt0126029", "sdfdsfg");
		String jsonToSend = mapper.writeValueAsString(dto);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reviews").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON).session(session);
		
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("Fields cannot be left blank"));
		
	}
	
	@Test
	public void testCreateReviewBlankText() throws Exception {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, 1);
		
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentUser", user);
		
		AddReviewDTO dto = new AddReviewDTO("title", null, "tt0126029", "sdfdsfg");
		String jsonToSend = mapper.writeValueAsString(dto);
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/reviews").content(jsonToSend)
				.contentType(MediaType.APPLICATION_JSON).session(session);
		
		
		this.mvc.perform(builder).andExpect(MockMvcResultMatchers.status().is(400))
			.andExpect(MockMvcResultMatchers.content().string("Fields cannot be left blank"));
		
	}
	

}
