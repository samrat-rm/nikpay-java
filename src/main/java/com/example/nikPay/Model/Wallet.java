package com.example.nikPay.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import com.example.nikPay.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.nikPay.Service.CurrencyService.convertCurrency;

@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private String currency;

    private String userID;

    private float amount;

    @ElementCollection
    private List<String> transactionIds;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float creditAmount(float amount, Currency currency) {
        Currency userCurrency = Currency.valueOf(this.currency);
        if (userCurrency == currency) {
            float previousAmount = this.getAmount();
            this.setAmount(previousAmount + amount);
        } else {
            float convertedAmount = convertCurrency(currency, userCurrency, amount);
            float previousAmount = this.getAmount();
            this.setAmount(previousAmount + convertedAmount);
        }
        return this.getAmount();
    }

    public float debitAmount(float amount, Currency currency) {
        Currency userCurrency = Currency.valueOf(this.currency);
        float updatedAmount;
        if (userCurrency == currency) {
            float previousAmount = this.getAmount();
            updatedAmount = previousAmount - amount;
        } else {
            float convertedAmount = convertCurrency(currency, userCurrency, amount);

            // Deduct the converted amount from the wallet
            float previousAmount = this.getAmount();
            updatedAmount = previousAmount - convertedAmount;
        }
        if (updatedAmount < 0) {
            throw new IllegalArgumentException("Insufficient funds. Cannot debit amount: " + amount);
        }
        this.amount = updatedAmount;
        System.out.println("Updated       " + this.amount);
        return this.getAmount();
    }

    public List<String> getTransactionIds() {
        return transactionIds;
    }

    public void setTransactionIds(List<String> transactionIds) {
        this.transactionIds = transactionIds;
    }

    public void addTransactionId(String transactionId) {
        this.transactionIds.add(transactionId);
    }

    public Wallet(String userID, String currency) {
        this.currency = currency;
        this.userID = userID;
        this.amount = 0f;
        this.transactionIds = new ArrayList<>();
    }

    public Wallet() {
    }

}
