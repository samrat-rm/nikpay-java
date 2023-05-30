package com.example.nikPay.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransferRecordsTest {

    @Test
    public void testTransferRecordsConstructor() {
        // Arrange
        float amount = 100.0f;
        String sender = "senderId";
        String receiver = "receiverId";
        String currency = "USD";

        // Act
        TransferRecords transferRecords = new TransferRecords(amount, sender, receiver, currency);

        // Assert
        Assertions.assertEquals(amount, transferRecords.getAmount());
        Assertions.assertEquals(sender, transferRecords.getSender());
        Assertions.assertEquals(receiver, transferRecords.getReceiver());
        Assertions.assertEquals(currency, transferRecords.getCurrency());
    }

    @Test
    public void testTransferRecordsSetterAndGetter() {
        // Arrange
        TransferRecords transferRecords = new TransferRecords();

        // Act
        transferRecords.setAmount(200.0f);
        transferRecords.setSender("senderId");
        transferRecords.setReceiver("receiverId");
        transferRecords.setCurrency("EUR");

        // Assert
        Assertions.assertEquals(200.0f, transferRecords.getAmount());
        Assertions.assertEquals("senderId", transferRecords.getSender());
        Assertions.assertEquals("receiverId", transferRecords.getReceiver());
        Assertions.assertEquals("EUR", transferRecords.getCurrency());
    }
}
