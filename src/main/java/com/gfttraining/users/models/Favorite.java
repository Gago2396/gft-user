package com.gfttraining.users.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "favorites")
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FavoritePK.class)
public class Favorite {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "product_id", nullable = false)
    private Long product;

}