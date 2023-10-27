package com.gfttraining.users.services;

import com.gfttraining.users.exceptions.CartConnectionRefusedException;
import com.gfttraining.users.exceptions.CartResponseFailedException;
import com.gfttraining.users.models.Cart;
import com.gfttraining.users.models.Status;
import com.gfttraining.users.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public int getFidelityPoints(Long user) throws CartResponseFailedException, CartConnectionRefusedException {

        List<Cart> cartList = cartRepository.getCartById(user);
        List<BigDecimal> pricesList = getCartPrices(cartList);
        return sumFidelityPoints(pricesList);
    }

    public List<BigDecimal> getCartPrices(List<Cart> cartList) throws CartResponseFailedException, CartConnectionRefusedException {
        return cartList.stream()
                .filter(cart -> cart.getStatus().equals(Status.SUBMITTED))
                .map(Cart::getFinalPrice)
                .collect(Collectors.toList());
    }

    public int sumFidelityPoints(List<BigDecimal> pricesList) {
        return pricesList.stream()
                .filter(finalPrice -> finalPrice.compareTo(new BigDecimal("20")) >= 0)
                .map(CartService::calculatePoints)
                .reduce(0, Integer::sum);
    }

    public static int calculatePoints(BigDecimal finalPrice) {
        if (finalPrice.compareTo(new BigDecimal("20")) >= 0 && finalPrice.compareTo(new BigDecimal("30")) < 0) {
            return 1;
        } else if (finalPrice.compareTo(new BigDecimal("30")) >= 0 && finalPrice.compareTo(new BigDecimal("50")) < 0) {
            return 3;
        } else if (finalPrice.compareTo(new BigDecimal("50")) >= 0 && finalPrice.compareTo(new BigDecimal("100")) < 0) {
            return 5;
        } else if (finalPrice.compareTo(new BigDecimal("100")) >= 0) {
            return 10;
        } else {
            return 0;
        }
    }
}
