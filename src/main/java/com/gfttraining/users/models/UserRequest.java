package com.gfttraining.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    private String lastName;
    private String address;
    @NotBlank(message = "Payment Method is required")
    private String paymentMethod;
    private int fidelityPoints;
    private double averagePurchase;

}
