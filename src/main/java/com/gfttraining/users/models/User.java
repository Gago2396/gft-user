package com.gfttraining.users.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;

    @ManyToOne
    @JoinColumn
    private Address address;

    @ManyToOne
    @JoinColumn
    private PaymentMethod paymentMethod;

    private Integer fidelityPoints;

    private Double averagePurchase;

}
