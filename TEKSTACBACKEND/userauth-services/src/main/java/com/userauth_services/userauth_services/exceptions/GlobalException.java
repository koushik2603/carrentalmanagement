package com.userauth_services.userauth_services.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalException {
@ExceptionHandler(AuthorizationDeniedException.class)
public ResponseEntity<String> AccessDenied()
{
	return new ResponseEntity<String>("No access",HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(SignatureException.class)
public ResponseEntity<String> SignatureDenied()
{
	return new ResponseEntity<String>("Invalid token",HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(JsonParseException.class)
public ResponseEntity<String> invalidJwt()
{
	return new ResponseEntity<String>("Invalid token",HttpStatus.BAD_REQUEST);
}
//ExpiredJwtException

@ExceptionHandler(ExpiredJwtException.class)
public ResponseEntity<String> TokenExpired()
{
	return new ResponseEntity<String>("Session Expired-please re-login",HttpStatus.BAD_REQUEST);
}


}
