package com.example.nikPay.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import com.example.nikPay.Currency;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.nikPay.Service.CurrencyService.convertCurrency;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String currency;

    private String userID;

    private float amount;

    @Column(length = 1000) // Adjust the length as per your needs
    private String transactionIds;

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
            System.out.println(previousAmount + "previousAmount");
            updatedAmount = previousAmount - convertedAmount;
            System.out.println(updatedAmount + "updatedAmount");
        }
        if (updatedAmount < 0) {
            throw new IllegalArgumentException("Insufficient funds. Cannot debit amount: " + amount);
        }
        this.amount = updatedAmount;
        System.out.println("Updated: " + this.amount);
        return this.getAmount();
    }

    public List<String> getTransactionIds() {
        if (transactionIds == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(transactionIds.split(",")));
    }

    public void setTransactionIds(List<String> transactionIds) {
        if (transactionIds == null || transactionIds.isEmpty()) {
            this.transactionIds = null;
        } else {
            this.transactionIds = String.join(",", transactionIds);
        }
    }

    public void addTransactionId(String transactionId) {
        List<String> ids = getTransactionIds();
        ids.add(transactionId);
        setTransactionIds(ids);
    }

    @Builder
    public Wallet(String userID, String currency) {
        this.currency = currency;
        this.userID = userID;
        this.amount = 0f;
        this.transactionIds = null;
    }
}
