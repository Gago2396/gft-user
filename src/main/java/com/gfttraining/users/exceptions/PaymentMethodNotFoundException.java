package com.gfttraining.users.exceptions;

public class PaymentMethodNotFoundException extends RuntimeException {

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }
}
