package com.revature.movietweeter.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Like {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private User liker;
	
	@ManyToOne
	private Review review;

	public Like() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Like(User liker, Review review) {
		super();
		this.liker = liker;
		this.review = review;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getLiker() {
		return liker;
	}

	public void setLiker(User liker) {
		this.liker = liker;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, liker, review);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Like other = (Like) obj;
		return id == other.id && Objects.equals(liker, other.liker) && Objects.equals(review, other.review);
	}

	@Override
	public String toString() {
		return "Like [id=" + id + ", liker=" + liker + ", review=" + review + "]";
	}
	
	
	
	
	
}
