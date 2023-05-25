package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import jakarta.persistence.EntityNotFoundException;


import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExpectationsHandler {
    
    // this method will try and  catch any method that throws EntityNotFoundException and return a 404
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO(e.getMessage()));
    }
    //Try and catch AuthenticationExceptions, returning 401
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO(e.getMessage()));
    }

}
