package com.revature.movietweeter.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.movietweeter.annotation.AuthorizedUser;
import com.revature.movietweeter.dto.AddReviewDTO;
import com.revature.movietweeter.exception.BlankFieldException;
import com.revature.movietweeter.exception.InvalidParameterException;
import com.revature.movietweeter.exception.InvalidRatingException;
import com.revature.movietweeter.exception.MovieNotFoundException;
import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;
import com.revature.movietweeter.service.ReviewService;

@RestController
public class ReviewController {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private ReviewService rs;

	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@GetMapping(path = "/movies/{id}/reviews")
	public ResponseEntity<Object> getAllReviewsByMovie(@PathVariable("id") String id) {
		List<Review> listOfReviewsByMovie = rs.getReviewsByMovie(id);
		return ResponseEntity.status(200).body(listOfReviewsByMovie);
	}

	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@GetMapping(path = "/reviews")
	public ResponseEntity<Object> getAllReviews() {
		List<Review> allReviews = rs.getReviews();
		return ResponseEntity.status(200).body(allReviews);
	}

	@AuthorizedUser
	@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
	@PostMapping(path = "/reviews")
	public ResponseEntity<Object> createReview(@RequestBody AddReviewDTO dto) {

		User currentlyLoggedInUser = (User) req.getSession().getAttribute("currentUser");

		Review createdReview;
		try {
			createdReview = rs.createReview(currentlyLoggedInUser, dto);

			return ResponseEntity.status(201).body(createdReview);
		} catch (InvalidRatingException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (MovieNotFoundException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (BlankFieldException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}

	}

	@GetMapping(path = "/users/{id}/reviews")
	public ResponseEntity<Object> getAllReviewsByAuthor(@PathVariable("id") String id) {
		List<Review> listOfReviewsByAuthor;
		try {
			listOfReviewsByAuthor = rs.getReviewsByAuthor(id);
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			return ResponseEntity.status(400).body(e.getMessage());
		}
		return ResponseEntity.status(200).body(listOfReviewsByAuthor);
	}

}
