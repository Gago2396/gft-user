package com.gfttraining.users.models;

import lombok.*;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {

    @NotBlank(message = "User is required")
    private Long user;
    @NotBlank(message = "Product is required")
    private Long product;

}