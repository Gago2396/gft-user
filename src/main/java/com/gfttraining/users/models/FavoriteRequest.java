package com.gfttraining.users.models;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {

    private Long user;
    private Long product;

}