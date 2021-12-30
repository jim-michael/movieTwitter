package com.revature.movietweeter.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseErrorDTO {
	@JsonProperty("Response")
	private String response;
	
	@JsonProperty("Error")
	private String error;

	public ResponseErrorDTO() {
		super();
	}

	public ResponseErrorDTO(String response, String error) {
		super();
		this.response = response;
		this.error = error;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public int hashCode() {
		return Objects.hash(error, response);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResponseErrorDTO other = (ResponseErrorDTO) obj;
		return Objects.equals(error, other.error) && Objects.equals(response, other.response);
	}

	@Override
	public String toString() {
		return "ResponseErrorDTO [response=" + response + ", error=" + error + "]";
	}
	
	
	
}
