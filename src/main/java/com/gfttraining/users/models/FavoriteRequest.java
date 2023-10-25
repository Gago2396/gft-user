package com.gfttraining.users.models;

import lombok.*;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {

    @NotNull(message = "User is required")
    private Long user;

    @NotNull(message = "Product is required")
    private Long product;

}