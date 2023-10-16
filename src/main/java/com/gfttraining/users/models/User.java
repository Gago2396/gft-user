package com.gfttraining.users.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    private Integer fidelityPoints;
    private Double averagePurchase;

    public User(Long id, String name, String lastName, String address, PaymentMethod paymentMethod, int fidelityPoints, double averagePurchase) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.fidelityPoints = fidelityPoints;
        this.averagePurchase = averagePurchase;
    }

    public User() {
    }

}
