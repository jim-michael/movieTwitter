package com.revature.movietweeter.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String reviewText;
	
	@Column(nullable = false)
	private int rating;
	
	@Column(nullable = false)
	private Date submissionTime;
	
	@Column(nullable = false)
	private String movieApiId;
	
	@ManyToOne
	private User author;

	public Review() {
		super();
	}

	public Review(String title, String reviewText, int rating, Date submissionTime, String movieApiId, User author) {
		super();
		this.title = title;
		this.reviewText = reviewText;
		this.rating = rating;
		this.submissionTime = submissionTime;
		this.movieApiId = movieApiId;
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	public String getMovieApiId() {
		return movieApiId;
	}

	public void setMovieApiId(String movieApiId) {
		this.movieApiId = movieApiId;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, id, movieApiId, rating, reviewText, submissionTime, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(author, other.author) && id == other.id && Objects.equals(movieApiId, other.movieApiId)
				&& rating == other.rating && Objects.equals(reviewText, other.reviewText)
				&& Objects.equals(submissionTime, other.submissionTime) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", title=" + title + ", reviewText=" + reviewText + ", rating=" + rating
				+ ", submissionTime=" + submissionTime + ", movieApiId=" + movieApiId + ", author=" + author + "]";
	}
	
	
}
