package com.gfttraining.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String street;
    private String city;
    private String province;
    private int postalCode;
    private Country country;
}
