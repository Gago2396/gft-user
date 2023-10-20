package com.gfttraining.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;
    private String lastName;
    private String street;
    private String city;
    private String province;
    private int postalCode;
    @NotBlank(message = "Country is required")
    private String country;
    @NotBlank(message = "Payment Method is required")
    private String paymentMethod;
    private int fidelityPoints;
    private double averagePurchase;

}
