package com.revature.movietweeter.daounittests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

import com.revature.movietweeter.dao.ReviewDAO;
import com.revature.movietweeter.dto.AddReviewDTO;
import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewDaoTest {
	@Autowired
	private EntityManager em;

	@Autowired
	private ReviewDAO sut;

	@Test
	@Transactional
	public void testGetAllReviews_positive() {

		User u = new User("connor_huston", "pass12345");
		this.em.persist(u);

		Date d = new Date();
		Review r = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		this.em.persist(r);

		Review r2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		this.em.persist(r2);

		User m = new User("mike", "67890word");
		this.em.persist(m);

		Review m1 = new Review("The Dark Knight", "Best movie ever", 5, d, "tt5", m);
		this.em.persist(m1);

		Review m2 = new Review("Man of Steel", "This movie was ok", 3, d, "tt6", m);
		this.em.persist(m2);

		this.em.flush();

		List<Review> reviews = this.sut.getAllReviews();

		Review expected1 = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		expected1.setId(1);

		Review expected2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		expected2.setId(2);

		Review expected3 = new Review("The Dark Knight", "Best movie ever", 5, d, "tt5", m);
		expected3.setId(3);

		Review expected4 = new Review("Man of Steel", "This movie was ok", 3, d, "tt6", m);
		expected4.setId(4);

		List<Review> expectedReviews = new ArrayList<>();
		expectedReviews.add(expected1);
		expectedReviews.add(expected2);
		expectedReviews.add(expected3);
		expectedReviews.add(expected4);

		Assertions.assertEquals(expectedReviews, reviews);
	}

	@Test
	@Transactional
	public void testGetReviewsByAuthor_positive() {

		User u = new User("connor_huston", "pass12345");
		this.em.persist(u);

		Date d = new Date();
		Review r = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		this.em.persist(r);

		Review r2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		this.em.persist(r2);

		this.em.flush();

		Review expected1 = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		expected1.setId(1);

		Review expected2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		expected2.setId(2);

		List<Review> expectedList = new ArrayList<>();
		expectedList.add(expected1);
		expectedList.add(expected2);
		

		List<Review> actualList = this.sut.getReviewsByAuthor(1);

		Assertions.assertEquals(expectedList, actualList);
	}

	@Test
	@Transactional
	public void testGetReviewsByAuthor_userDoesNotExist() {

		List<Review> actual = this.sut.getReviewsByAuthor(1);

		List<Review> expected = new ArrayList<>();

		Assertions.assertEquals(expected, actual);
	}

	@Test
	@Transactional
	public void testAddReviewForUser_positive() {

		User u = new User("connor_huston", "pass12345");
		this.em.persist(u);

		this.em.flush();

		Review addedReview = this.sut.addReview(u, 5, "Batman", "Best movie I've ever seen", "tt3");

		Date d = addedReview.getSubmissionTime();
		Review expected = new Review("Batman", "Best movie I've ever seen", 5, d, "tt3", u);
		expected.setId(1);

		Assertions.assertEquals(expected, addedReview);
	}

	@Test
	@Transactional
	public void testGetAllReviewsByMovie_positive() {

		User u = new User("connor_huston", "pass12345");
		this.em.persist(u);

		Date d = new Date();
		Review r = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		this.em.persist(r);

		Review r2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		this.em.persist(r2);

		this.em.flush();

		Review expected1 = new Review("Spiderman", "Best movie I've ever seen", 5, d, "tt3", u);
		expected1.setId(1);

		Review expected2 = new Review("Jack and Jill", "This movie is the worst!", 1, d, "tt4", u);
		expected2.setId(2);

		List<Review> expectedList = new ArrayList<>();
		expectedList.add(expected1);
	

		List<Review> actualList = this.sut.getReviewsByMovie("tt3");

		Assertions.assertEquals(expectedList, actualList);
	}
}
