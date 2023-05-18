package com.example.nikPay.Model;

import com.example.nikPay.Model.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WalletTest {

    @Test
    public void testSetAndGetUserID() {
        // Arrange
        Wallet wallet = new Wallet();

        // Act
        wallet.setUserID("202d8aa3-89d2-4497-8880-97194fc8aa5c");

        // Assert
        Assertions.assertEquals("202d8aa3-89d2-4497-8880-97194fc8aa5c", wallet.getUserID());
    }

    @Test
    public void testSetAndGetAmount() {
        // Arrange
        Wallet wallet = new Wallet();

        // Act
        wallet.setAmount(100.0f);

        // Assert
        Assertions.assertEquals(100.0f, wallet.getAmount());
    }

    @Test
    public void testConstructorWithUserID() {
        // Arrange
        String userID = "202d8aa3-89d2-4497-8880-97194fc8aa5c";

        // Act
        Wallet wallet = new Wallet(userID);

        // Assert
        Assertions.assertEquals(userID, wallet.getUserID());
        Assertions.assertEquals(0.0f, wallet.getAmount());
    }
}
