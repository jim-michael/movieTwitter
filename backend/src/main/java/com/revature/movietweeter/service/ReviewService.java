package com.revature.movietweeter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.revature.movietweeter.dao.ReviewDAO;
import com.revature.movietweeter.dto.AddReviewDTO;
import com.revature.movietweeter.dto.ResponseErrorDTO;
import com.revature.movietweeter.exception.BlankFieldException;
import com.revature.movietweeter.exception.InvalidParameterException;
import com.revature.movietweeter.exception.InvalidRatingException;
import com.revature.movietweeter.exception.MovieNotFoundException;
import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;

@Service
public class ReviewService {

	@Autowired
	private ReviewDAO rd;
	
	public ReviewService(ReviewDAO mockReviewDao) {
		this.rd = mockReviewDao;
	}

	public List<Review> getReviews() {
		return rd.getAllReviews();
	}

	public Review createReview(User currentlyLoggedInUser, AddReviewDTO dto)
			throws InvalidRatingException, IOException, MovieNotFoundException, BlankFieldException {
		String url = "http://www.omdbapi.com/?i=" + dto.getApiId() + "&apikey=50275210";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			String output = inputLine;

			try {
				ResponseErrorDTO response = new ObjectMapper().readValue(output, ResponseErrorDTO.class);
				throw new MovieNotFoundException("movie with imdbId does not exist");
			} catch (UnrecognizedPropertyException e) {
				//Title was successfully found
			}
		}

		in.close();
		
		if (dto.getTitle() == null || dto.getReviewText() == null) {
			throw new BlankFieldException("Fields cannot be left blank");
		}

		try {
			int rating = Integer.parseInt(dto.getRating());
			if (rating > 5 || rating <= 0) {
				throw new InvalidRatingException("Rating must be integer 1-5");
			}
			return rd.addReview(currentlyLoggedInUser, rating, dto.getTitle(), dto.getReviewText(), dto.getApiId());
		} catch (NumberFormatException e) {
			throw new InvalidRatingException("Rating must be integer 1-5");
		}
		
		
		

	}

	public List<Review> getReviewsByMovie(String id) {
		return rd.getReviewsByMovie(id);
	}

	public List<Review> getReviewsByAuthor(String id) throws InvalidParameterException {
		
		try {
			int authorId = Integer.parseInt(id);
			return rd.getReviewsByAuthor(authorId);
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("invalid author id. Id must be integer");
		}
		
	}
}
