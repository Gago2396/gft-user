package com.gfttraining.users.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRequest {

    private Long user;
    private Long productId;

}