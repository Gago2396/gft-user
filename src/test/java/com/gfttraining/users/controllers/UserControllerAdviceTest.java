package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("GIVEN ConstraintViolationException WHEN handleConstraintViolationException THEN return BAD_REQUEST response")
    public void testHandleConstraintViolationException() {
        UserControllerAdvice advice = new UserControllerAdvice();

        ConstraintViolationException ex = new ConstraintViolationException("Mensaje de excepción", new HashSet<>());

        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getMessage()).thenReturn("La restricción no se cumplió");
        ex.getConstraintViolations().add(constraintViolation);

        ResponseEntity<String> response = advice.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN MethodArgumentNotValidException WHEN handleMethodArgumentNotValidException THEN return BAD_REQUEST response")
    public void testHandleMethodArgumentNotValidException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        ResponseEntity<String> response = advice.handleMethodArgumentNotValidException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN NoSuchElementException WHEN handleNoSuchElementException THEN return NOT_FOUND response")
    public void testHandleNoSuchElementException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        NoSuchElementException ex = mock(NoSuchElementException.class);

        ResponseEntity<String> response = advice.NoSuchElementException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN CartConnectionRefusedException WHEN handleCartConnectionRefusedException THEN return INTERNAL_SERVER_ERROR response")
    public void testCartConnectionRefusedException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        CartConnectionRefusedException ex = mock(CartConnectionRefusedException.class);

        ResponseEntity<String> response = advice.CartConnectionRefusedException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN CountryNotFoundException WHEN handleCountryNotFoundException THEN return NOT_FOUND response")
    public void testHandleCountryNotFoundException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        CountryNotFoundException ex = mock(CountryNotFoundException.class);

        ResponseEntity<String> response = advice.CountryNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN CartResponseFailedException WHEN handleCartResponseFailedException THEN return BAD_REQUEST response")
    public void testCartResponseFailedException() {
        UserControllerAdvice advice = new UserControllerAdvice();
        CartResponseFailedException ex = mock(CartResponseFailedException.class);

        ResponseEntity<String> response = advice.CartResponseFailedException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
