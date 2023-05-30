package com.example.nikPay.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class TransferRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    private float amount;

    private String sender;

    private String receiver;

    private String currency;

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public float getAmount() {
        return amount;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }


    public TransferRecords(float amount, String sender, String receiver, String currency) {
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.currency = currency;
    }

    public TransferRecords() {
    }
}
