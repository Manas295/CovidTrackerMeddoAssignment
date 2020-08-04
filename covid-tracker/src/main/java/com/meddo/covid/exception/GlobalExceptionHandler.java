package com.meddo.covid.exception;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
@ControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(BadCredentialsException.class)
 public ResponseEntity<?> resourceNotFoundException(BadCredentialsException ex, WebRequest request) {
  ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
  return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
 }
 @ExceptionHandler(Exception.class)
 public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
  ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
  return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
 }
}