package com.gfttraining.users.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    private PaymentMethod paymentMethodTest;

    @BeforeEach
    void setUp(){
        paymentMethodTest = new PaymentMethod(1L,"Credit Card");
    }

    @Test
    void testGetId() {
        assertEquals(1L, paymentMethodTest.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Credit Card", paymentMethodTest.getName());
    }

    @Test
    void testSetId() {
        paymentMethodTest.setId(2L);
        assertEquals(2L, paymentMethodTest.getId());
    }

    @Test
    void testSetName() {
        paymentMethodTest.setName("New Payment Method");
        assertEquals("New Payment Method", paymentMethodTest.getName());
    }
}
