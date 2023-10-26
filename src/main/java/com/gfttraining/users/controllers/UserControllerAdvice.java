package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {

        StringBuilder errorMessage = new StringBuilder("Validation error(s):\n");
        ex.getConstraintViolations().forEach(violation ->
                errorMessage.append("\s").append(violation.getMessage()).append(";\n")
        );

        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        StringBuilder errorMessage = new StringBuilder("Validation error(s):\n");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errorMessage.append("\s").append(error.getDefaultMessage()).append(";\n");
        });

        return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> NoSuchElementException(NoSuchElementException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<String> CountryNotFoundException(CountryNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartConnectionRefusedException.class)
    public ResponseEntity<String> CartConnectionRefusedException(CartConnectionRefusedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CartResponseFailedException.class)
    public ResponseEntity<String> CartResponseFailedException(CartConnectionRefusedException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}