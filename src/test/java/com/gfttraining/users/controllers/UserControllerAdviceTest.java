package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.CountryNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerAdviceTest {

    private UserController userController;

    @Test
    public void testHandleConstraintViolationException() {
        UserControllerAdvice advice = new UserControllerAdvice();

        // Crea una instancia de ConstraintViolationException con al menos una violación de restricción
        ConstraintViolationException ex = new ConstraintViolationException("Mensaje de excepción", new HashSet<>());

        // Agrega una violación de restricción (en este caso, una violación de restricción de ejemplo)
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getMessage()).thenReturn("La restricción no se cumplió");
        ex.getConstraintViolations().add(constraintViolation);

        ResponseEntity<String> response = advice.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


//    @Test
//    public void testHandleMethodArgumentNotValidException() {
//        UserControllerAdvice advice = new UserControllerAdvice();
//        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
//
//        ResponseEntity<String> response = advice.handleMethodArgumentNotValidException(ex);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }

    @Test
    public void testHandleNoSuchElementException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        NoSuchElementException ex = mock(NoSuchElementException.class);

        ResponseEntity<String> response = advice.NoSuchElementException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testHandleCountryNotFoundException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        CountryNotFoundException ex = mock(CountryNotFoundException.class);

        ResponseEntity<String> response = advice.CountryNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
