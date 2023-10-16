package com.gfttraining.users.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    private PaymentMethod paymentMethodTest;

    @BeforeEach
    void setUp(){
        paymentMethodTest = new PaymentMethod(1L,"Credit Card");
    }

    @Test
    @DisplayName("Test testGetId() in PaymentMethod")
    void testGetId() {
        assertEquals(1L, paymentMethodTest.getId());
    }

    @Test
    @DisplayName("Test testGetName() in PaymentMethod")
    void testGetName() {
        assertEquals("Credit Card", paymentMethodTest.getName());
    }

    @Test
    @DisplayName("Test testSetId() in PaymentMethod")
    void testSetId() {
        paymentMethodTest.setId(2L);
        assertEquals(2L, paymentMethodTest.getId());
    }

    @Test
    @DisplayName("Test testSetName() in PaymentMethod")
    void testSetName() {
        paymentMethodTest.setName("New Payment Method");
        assertEquals("New Payment Method", paymentMethodTest.getName());
    }
}
