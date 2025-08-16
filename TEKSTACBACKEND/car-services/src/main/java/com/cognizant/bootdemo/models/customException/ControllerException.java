package com.cognizant.bootdemo.models.customException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ControllerException {
	@ExceptionHandler(UserException.class)
	public ResponseEntity<String> handleUserException(UserException usercepException) {
		return new ResponseEntity<String>(usercepException.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleUserException() {
		return new ResponseEntity<String>("Registration Number Already exist", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleBookingConflict(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleNotFound(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<String> InvalidAPI() {
		return new ResponseEntity<String>("Invalid API Request", HttpStatus.BAD_REQUEST);
	}


}