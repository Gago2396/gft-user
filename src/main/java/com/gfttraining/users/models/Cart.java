package com.gfttraining.users.models;

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
}
