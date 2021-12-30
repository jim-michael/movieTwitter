package com.revature.movietweeter.dto;

import java.util.Objects;

public class AddReviewDTO {
	private String title;
	private String reviewText;
	private String apiId;
	private String rating;
	
	public AddReviewDTO() {
		super();
	}

	public AddReviewDTO(String title, String reviewText, String apiId, String rating) {
		super();
		this.title = title;
		this.reviewText = reviewText;
		this.apiId = apiId;
		this.rating = rating;
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

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apiId, rating, reviewText, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddReviewDTO other = (AddReviewDTO) obj;
		return Objects.equals(apiId, other.apiId) && rating == other.rating
				&& Objects.equals(reviewText, other.reviewText) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "AddReviewDTO [title=" + title + ", reviewText=" + reviewText + ", apiId=" + apiId + ", rating=" + rating
				+ "]";
	}
	
	
	
	
	
	
}
