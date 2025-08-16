package com.API_gateway.API_gateway.exceptions;

import java.net.ConnectException;
import java.util.Map;

import org.apache.http.auth.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
//import org.springframework.security.core.AuthenticationException;

import io.jsonwebtoken.ExpiredJwtException;


@ControllerAdvice
public class GlobalExceptions {
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> runtimeexception()
	{
		return new ResponseEntity<String>("No access",HttpStatus.BAD_REQUEST);
	}
	  @ExceptionHandler(ConnectException.class)
	    public ResponseEntity<?> handleConnectException(ConnectException ex) {
	        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
	                .body(Map.of("error", "Service Unavailable", "message", ex.getMessage()));
	    }

	    @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<?> handleAuthException(AuthenticationException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", "Unauthorized", "message", ex.getMessage()));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<?> handleGenericException(Exception ex) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "Server Error", "message", ex.getMessage()));
	    }
	    @ExceptionHandler(WebClientResponseException.class)
	    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
	        return new ResponseEntity<>("External API error: " + ex.getMessage(), ex.getStatusCode());
	    }
	    @ExceptionHandler(WebClientRequestException.class)
	    public ResponseEntity<String> handleWebClientRequestException(WebClientRequestException ex) {
	        return new ResponseEntity<>("Network error while calling external service: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
	    }

	    @ExceptionHandler(AuthenticationException.class)
	    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
	        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
	    }

@ExceptionHandler(ExpiredJwtException.class)
public ResponseEntity<String> TokenExpired()
{
	return new ResponseEntity<String>("Session Expired-please re-login",HttpStatus.BAD_REQUEST);
}

	    

	    
}
