package com.gfttraining.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;
    private String name;
    private String lastName;
    private String address;
    private String paymentMethod;
    private int fidelityPoints;
    private double averagePurchase;

}
