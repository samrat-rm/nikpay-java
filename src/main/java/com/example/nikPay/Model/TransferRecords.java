package com.example.nikPay.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    public TransferRecords(float amount, String sender, String receiver, String currency) {
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
        this.currency = currency;
    }
}
