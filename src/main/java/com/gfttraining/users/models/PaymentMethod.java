package com.gfttraining.users.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "payment_methods")
@Data
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public PaymentMethod(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public PaymentMethod() {
    }
}
