package com.revature.dto;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.service.AccountService;

public class ExceptionMessageDTO {

	private String message;

	private Logger logger = LoggerFactory.getLogger(AccountService.class); //for logging tests
	
	// Constructors
	public ExceptionMessageDTO() {
		logger.info("ExceptionMessageDTO() invoked in Exceptions");
	}

	public ExceptionMessageDTO(Exception e) {
		this.message = e.getMessage();
		logger.info("ExceptionMessageDTO() invoked in Exceptions");
	}

	// Methods
	
	public String getMessage() {
		return message;
		
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ExceptionMessageDTO [message=" + message + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(message);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExceptionMessageDTO other = (ExceptionMessageDTO) obj;
		return Objects.equals(message, other.message);
	}

}
