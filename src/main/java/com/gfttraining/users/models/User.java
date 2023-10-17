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

    @NotBlank(message = "Name is required")
    private String name;

    private String lastName;

    private String address;


    @ManyToOne
    @JoinColumn
    private PaymentMethod paymentMethod;

    private Integer fidelityPoints;

    private Double averagePurchase;

}
