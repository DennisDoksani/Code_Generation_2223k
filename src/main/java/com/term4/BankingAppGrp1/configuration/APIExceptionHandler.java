package com.term4.BankingAppGrp1.configuration;

import com.term4.BankingAppGrp1.responseDTOs.ErrorMessageDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.LimitExceededException;

@ControllerAdvice
public class APIExceptionHandler {
    // this method will try and  catch any method that throws EntityNotFoundException and return a 404
    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO(e.getMessage()));
    }
    // sending conflict status code when limit is exceeded
    @ExceptionHandler(value = {LimitExceededException.class})
    public ResponseEntity<Object> handleException(LimitExceededException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
    }


}
