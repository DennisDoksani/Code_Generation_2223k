package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import jakarta.persistence.EntityNotFoundException;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.naming.LimitExceededException;

@RestControllerAdvice
public class APIExceptionHandler {
    // this method will try and  catch any method that throws EntityNotFoundException and return a 404
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO(e.getMessage()));
    }

    // sending conflict status code when limit is exceeded
    @ExceptionHandler(value = {LimitExceededException.class})
    public ResponseEntity<Object> handleException(LimitExceededException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO(e.getMessage()));
    }
    // All the Exceptions that are related to Jakarta Binding Exception
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessageDTO(e.getMessage()));
    }
}
