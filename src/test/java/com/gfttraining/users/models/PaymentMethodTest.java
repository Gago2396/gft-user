package com.gfttraining.users.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    void testGetId() {
        PaymentMethod paymentMethod = new PaymentMethod();
        Long id = paymentMethod.getId();
        assertNull(id);
    }

    @Test
    void testGetName() {
        PaymentMethod paymentMethod = new PaymentMethod();
        String name = paymentMethod.getName();
        assertNull(name);
    }

    @Test
    void testSetId() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId(1L);
        assertEquals(1L, paymentMethod.getId());
    }

    @Test
    void testSetName() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setName("Tarjeta de Crédito");
        assertEquals("Tarjeta de Crédito", paymentMethod.getName());
    }
}
