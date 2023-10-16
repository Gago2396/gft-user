package com.gfttraining.users.models;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoritePK implements Serializable {

    private User user;

    private long productId;

    public FavoritePK(User user, long productId) {
        super();
        this.user = user;
        this.productId = productId;
    }

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
