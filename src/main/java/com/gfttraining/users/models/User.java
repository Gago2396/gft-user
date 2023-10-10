package com.gfttraining.users.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    private int fidelityPoints;
    private double averagePurchase;

    public User(Long id, String name, String lastName, String address, PaymentMethod paymentMethod, int fidelityPoints, double averagePurchase) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.fidelityPoints = fidelityPoints;
        this.averagePurchase = averagePurchase;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getFidelityPoints() {
        return fidelityPoints;
    }

    public void setFidelityPoints(int fidelityPoints) {
        this.fidelityPoints = fidelityPoints;
    }

    public double getAveragePurchase() {
        return averagePurchase;
    }

    public void setAveragePurchase(double averagePurchase) {
        this.averagePurchase = averagePurchase;
    }
}
