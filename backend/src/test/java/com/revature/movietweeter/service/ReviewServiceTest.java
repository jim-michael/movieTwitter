package com.revature.movietweeter.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.revature.movietweeter.dao.ReviewDAO;
import com.revature.movietweeter.dto.AddReviewDTO;
import com.revature.movietweeter.exception.BlankFieldException;
import com.revature.movietweeter.exception.InvalidParameterException;
import com.revature.movietweeter.exception.InvalidRatingException;
import com.revature.movietweeter.exception.MovieNotFoundException;
import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;

public class ReviewServiceTest {

	private ReviewService sut;

	@Test
	public void testCreateReview()
			throws InvalidRatingException, IOException, MovieNotFoundException, BlankFieldException {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		User u = new User("user", "password");

		Date d = new Date();

		when(mockReviewDao.addReview(eq(u), eq(5), eq("title"), eq("text"), eq("tt0126029")))
				.thenReturn(new Review("title", "text", 5, d, "tt0126029", u));

		ReviewService rs = new ReviewService(mockReviewDao);

		Review actual = rs.createReview(u, new AddReviewDTO("title", "text", "tt0126029", "5"));

		Review expected = new Review("title", "text", 5, d, "tt0126029", u);

		Assertions.assertEquals(expected, actual);
	}
	
	@Test
	public void testCreateReviewMovieNotFound() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");

		
		Assertions.assertThrows(MovieNotFoundException.class, () -> {
			rs.createReview(u, new AddReviewDTO("title", "text", "wrongId", "5"));
		});
	}
	
	@Test
	public void testCreateReviewTitleBlank() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");
		
		Assertions.assertThrows(BlankFieldException.class, () -> {
			rs.createReview(u, new AddReviewDTO(null, "text", "tt0126029", "5"));
		});

	}
	
	@Test
	public void testCreateReviewTextBlank() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");
		
		Assertions.assertThrows(BlankFieldException.class, () -> {
			rs.createReview(u, new AddReviewDTO("title", null, "tt0126029", "5"));
		});

	}
	
	@Test
	public void testCreateReviewInvalidRatingString() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");
		
		Assertions.assertThrows(InvalidRatingException.class, () -> {
			rs.createReview(u, new AddReviewDTO("title", "text", "tt0126029", "sdfs"));
		});

	}
	
	@Test
	public void testCreateReviewInvalidRatingLessThanOne() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");
		
		Assertions.assertThrows(InvalidRatingException.class, () -> {
			rs.createReview(u, new AddReviewDTO("title", "text", "tt0126029", "0"));
		});

	}
	
	@Test
	public void testCreateReviewInvalidRatingGreaterThanFive() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		ReviewService rs = new ReviewService(mockReviewDao);
		
		User u = new User("user", "password");
		
		Assertions.assertThrows(InvalidRatingException.class, () -> {
			rs.createReview(u, new AddReviewDTO("title", "text", "tt0126029", "6"));
		});

	}
	
	@Test
	public void testGetReviews() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		
		Date d = new Date();
		User u = new User("user", "password");
		Review r1 = new Review("title", "text", 5, d, "ttsdlfs", u);
		Review r2 = new Review("title2", "text2", 4, d, "ttv", u);
		
		List<Review> reviews = new ArrayList<>();
		reviews.add(r1);
		reviews.add(r2);
		
		when(mockReviewDao.getAllReviews()).thenReturn(reviews);
		
		ReviewService rs = new ReviewService(mockReviewDao);
		
		List<Review> actual = rs.getReviews();
		
		Assertions.assertEquals(reviews, actual);
	}
	
	@Test
	public void testGetReviewsByMovie() {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		
		Date d = new Date();
		User u = new User("user", "password");
		Review r1 = new Review("title", "text", 5, d, "ttsdlfs", u);
		Review r2 = new Review("title2", "text2", 4, d, "ttv", u);
		
		List<Review> reviews = new ArrayList<>();
		reviews.add(r2);
		
		when(mockReviewDao.getReviewsByMovie(eq("ttv"))).thenReturn(reviews);
		
		ReviewService rs = new ReviewService(mockReviewDao);
		
		List<Review> actual = rs.getReviewsByMovie("ttv");
		
		Assertions.assertEquals(reviews, actual);
	}
	
	@Test
	public void testGetReviewsByAuthor() throws InvalidParameterException {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		
		Date d = new Date();
		User u = new User("user", "password");
		u.setId(1);
		Review r1 = new Review("title", "text", 5, d, "ttsdlfs", u);
		Review r2 = new Review("title2", "text2", 4, d, "ttv", u);
		
		
		List<Review> reviews = new ArrayList<>();
		reviews.add(r1);
		reviews.add(r2);
		
		when(mockReviewDao.getReviewsByAuthor(eq(1))).thenReturn(reviews);
		
		ReviewService rs = new ReviewService(mockReviewDao);
		
		List<Review> actual = rs.getReviewsByAuthor("1");
		
		Assertions.assertEquals(reviews, actual);
	}
	
	@Test
	public void testGetReviewsByAuthorInvalidId() throws InvalidParameterException {
		ReviewDAO mockReviewDao = mock(ReviewDAO.class);
		
		ReviewService rs = new ReviewService(mockReviewDao);
		
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			rs.getReviewsByAuthor("sfdgsdfg");
		});
	}
	
	
}
