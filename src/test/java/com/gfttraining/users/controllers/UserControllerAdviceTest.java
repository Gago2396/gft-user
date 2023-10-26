package com.gfttraining.users.controllers;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.exceptions.CountryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerAdviceTest {
    @Mock
    private BindingResult bindingResult;

    @Mock
    MethodArgumentNotValidException methodArgumentNotValidException ;

    private UserControllerAdvice userControllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userControllerAdvice = new UserControllerAdvice();
    }

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
    public void testHandleMethodArgumentNotValidException() {
        FieldError fieldError = new FieldError("objectName", "fieldName", "error message");

        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<String> response = userControllerAdvice.handleMethodArgumentNotValidException(methodArgumentNotValidException);

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
