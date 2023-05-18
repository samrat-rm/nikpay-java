package com.example.nikPay.Model;

import com.example.nikPay.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @JsonProperty("userID")
    String userID ;


    String currency;


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

    public void setCurrency(Currency currency) {
        this.currency = currency.name();
    }

    public Currency getCurrency() {
        return Currency.valueOf(this.currency);
    }

    public Wallet(String userID, Currency currency) {
        System.out.println(userID + currency);
        this.userID = userID;
        this.currency = currency.name();
        this.amount = 0f;
    }
}
