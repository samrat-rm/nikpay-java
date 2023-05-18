package com.example.nikPay.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonProperty("userID")
    String userID ;


    public void setAmount(float amount) {
        this.amount = amount;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public float getAmount() {
        return amount;
    }

    float amount;

    public Wallet() {
        // Default constructor
    }


    public Wallet(String userID) {
        this.userID = userID;
        this.amount = 0;
    }

}
