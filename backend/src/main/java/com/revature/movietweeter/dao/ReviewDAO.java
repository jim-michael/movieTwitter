package com.revature.movietweeter.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.revature.movietweeter.model.Review;
import com.revature.movietweeter.model.User;

@Repository
public class ReviewDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public List<Review> getAllReviews() {
		List<Review> listOfReviews = em.createQuery("FROM Review r ORDER BY r.submissionTime DESC", Review.class).getResultList();
		return listOfReviews;
	}
	
	@Transactional
	public Review addReview(User user, int rating, String title, String reviewText, String apiId) {
		Date currentDate = new Date();
		Review reviewToAdd = new Review(title, reviewText, rating, currentDate, apiId, user);
		em.persist(reviewToAdd);
		
		//User userWithoutPass = new User(user.getUsername(), user.getId());
		
		//Review reviewToSendBack = new Review(title, reviewText, rating, currentDate, apiId, userWithoutPass );
		return reviewToAdd;
	}
	
	@Transactional
	public List<Review> getReviewsByMovie(String id) {
		List<Review> listOfReviewsByMovie = em.createQuery("FROM Review r WHERE r.movieApiId = :id ORDER BY r.submissionTime DESC", Review.class).setParameter("id", id).getResultList();
		return listOfReviewsByMovie;
	}
	
	@Transactional
	public List<Review> getReviewsByAuthor(int authorId) {
		
		User author = em.find(User.class, authorId);
		List<Review> listOfReviewsByAuthor = em.createQuery("FROM Review r WHERE r.author = :user", Review.class).setParameter("user", author).getResultList();
		return listOfReviewsByAuthor;
	}
}
