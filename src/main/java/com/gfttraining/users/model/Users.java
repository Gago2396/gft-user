package com.gfttraining.users.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String direccion;
    private String metodoPago;
    private int puntosFidelidad;
    private double compraMedia;

    public Users(Long id, String nombre, String apellidos, String direccion, String metodoPago, int puntosFidelidad, double compraMedia) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.metodoPago = metodoPago;
        this.puntosFidelidad = puntosFidelidad;
        this.compraMedia = compraMedia;
    }

    public Users() {
    }
}
