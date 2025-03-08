package com.siddarthmishra.springboot.dto;

import java.io.Serializable;

public class UserDetailsDTO implements Serializable {

	private static final long serialVersionUID = 1390883757531634627L;

	private String emailId;

	private String firstName;

	private String lastName;

	private Integer userId;

	public String getEmailId() {
		return emailId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return String.format("UserDetailsDTO [userId=%s, emailId=%s, firstName=%s, lastName=%s]", userId, emailId,
				firstName, lastName);
	}
}
