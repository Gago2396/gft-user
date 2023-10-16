package com.gfttraining.users.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Set variables in User constructor")
    void constructorShouldSetValues() {
        Long id = 1L;
        String name = "John";
        String lastName = "Doe";
        String address = "123 Main St";
        PaymentMethod paymentMethod = new PaymentMethod();
        int fidelityPoints = 100;
        double averagePurchase = 50.0;

        User user = new User(id, name, lastName, address, paymentMethod, fidelityPoints, averagePurchase);

        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(lastName, user.getLastName());
        assertEquals(address, user.getAddress());
        assertEquals(paymentMethod, user.getPaymentMethod());
        assertEquals(fidelityPoints, user.getFidelityPoints());
        assertEquals(averagePurchase, user.getAveragePurchase());
    }

    @Test
    @DisplayName("Test getId() in User")
    void getId() {
        User user = new User();
        Long id = user.getId();
        assertNull(id);
    }

    @Test
    @DisplayName("Test getName() in User")
    void getName() {
        User user = new User();
        String name = user.getName();
        assertNull(name);
    }

    @Test
    @DisplayName("Test getLastName() in User")
    void getLastName() {
        User user = new User();
        String lastName = user.getLastName();
        assertNull(lastName);
    }

    @Test
    @DisplayName("Test getAddress() in User")
    void getAddress() {
        User user = new User();
        String address = user.getAddress();
        assertNull(address);
    }

    @Test
    @DisplayName("Test getPaymentMethod() in User")
    void getPaymentMethod() {
        User user = new User();
        PaymentMethod paymentMethod = user.getPaymentMethod();
        assertNull(paymentMethod);
    }

    @Test
    @DisplayName("Test getFidelityPoints() in User")
    void getFidelityPoints() {
        User user = new User();
        int fidelityPoints = user.getFidelityPoints();
        assertEquals(0, fidelityPoints);
    }

    @Test
    @DisplayName("Test getAveragePurchase() in User")
    void getAveragePurchase() {
        User user = new User();
        double averagePurchase = user.getAveragePurchase();
        assertEquals(0.0, averagePurchase);
    }

    @Test
    @DisplayName("Test setId() in User")
    void setId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Test setName() in User")
    void setName() {
        User user = new User();
        user.setName("John");
        assertEquals("John", user.getName());
    }

    @Test
    @DisplayName("Test setLastName() in User")
    void setLastName() {
        User user = new User();
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    @DisplayName("Test setAddress() in User")
    void setAddress() {
        User user = new User();
        user.setAddress("123 Main St");
        assertEquals("123 Main St", user.getAddress());
    }

    @Test
    @DisplayName("Test setPaymentMethod() in User")
    void setPaymentMethod() {
        User user = new User();
        PaymentMethod paymentMethod = new PaymentMethod();
        user.setPaymentMethod(paymentMethod);
        assertEquals(paymentMethod, user.getPaymentMethod());
    }

    @Test
    @DisplayName("Test setFidelityPoints() in User")
    void setFidelityPoints() {
        User user = new User();
        user.setFidelityPoints(100);
        assertEquals(100, user.getFidelityPoints());
    }

    @Test
    @DisplayName("Test setAveragePurchase() in User")
    void setAveragePurchase() {
        User user = new User();
        user.setAveragePurchase(50.0);
        assertEquals(50.0, user.getAveragePurchase());
    }
}
