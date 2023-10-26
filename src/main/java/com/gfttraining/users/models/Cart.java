package com.gfttraining.users.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Cart {
    private long id;
    private long userId;
    private Status status;
    private BigDecimal finalPrice;
    private BigDecimal finalWeight;

    @JsonCreator
    public static Cart createCart(@JsonProperty("id") Long id, @JsonProperty("userId") Long userId, @JsonProperty("status") Status status, @JsonProperty("finalPrice") String finalPrice, @JsonProperty("finalWeight") String finalWeight) {
        return new Cart(id, userId, status, new BigDecimal(finalPrice), new BigDecimal(finalWeight));
    }
}
