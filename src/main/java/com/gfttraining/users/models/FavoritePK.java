package com.gfttraining.users.models;

import java.io.Serializable;
import java.util.Objects;

public class FavoritePK implements Serializable {

    private Long user;

    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePK that = (FavoritePK) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, productId);
    }

}
