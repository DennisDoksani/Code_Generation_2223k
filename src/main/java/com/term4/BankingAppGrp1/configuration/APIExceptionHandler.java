package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.naming.LimitExceededException;

@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {LimitExceededException.class})
    public ResponseEntity<Object> handleException(LimitExceededException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException e) {
        String errorMessage = null;
        // gets the Message From Binding Exception which will be thrown by the Jakarta Validation
        if (!e.getBindingResult().getFieldErrors().isEmpty()) {
            FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
            errorMessage = fieldError.getDefaultMessage();
        } else if (!e.getBindingResult().getGlobalErrors().isEmpty()) {
            ObjectError objectError = e.getBindingResult().getGlobalErrors().get(0);
            errorMessage = objectError.getDefaultMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO(errorMessage));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessageDTO("Access Denied"));
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorMessageDTO("Method Not Allowed"));
    }

    @ExceptionHandler(value={ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(ConstraintViolationException e){
        String error = "";
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
             error = violation.getMessage();
        } // this will return the first error message
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessageDTO(error));
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<Object> handleException(EntityExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
    }
}
