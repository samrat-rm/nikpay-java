package com.example.nikPay.Model;

import com.example.nikPay.Currency;
import com.example.nikPay.Service.CurrencyService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static com.example.nikPay.Service.CurrencyService.convertCurrency;





public class WalletTest {
    @InjectMocks
    private Wallet wallet;
    @Mock
    private CurrencyService currencyService;


    @Test
    public void testGettersAndSetters() {
        // Arrange
        Wallet wallet = new Wallet();

        // Set values using setters
        wallet.setCurrency("USD");
        wallet.setUserID("123");
        wallet.setAmount(100.0f);

        // Assert
        Assert.assertEquals("USD", wallet.getCurrency());
        Assert.assertEquals("123", wallet.getUserID());
        Assert.assertEquals(100.0f, wallet.getAmount(), 0.01);
    }

    @Test
    public void testConstructor() {
        // Arrange
        String userID = "123";
        String currency = "USD";

        // Act
        Wallet wallet = new Wallet(userID, currency);

        // Assert
        Assert.assertEquals(userID, wallet.getUserID());
        Assert.assertEquals(currency, wallet.getCurrency());
        Assert.assertEquals(0.0f, wallet.getAmount(), 0.01);
    }

    @Test
    public void testCreditAmount_SameCurrency() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setAmount(100.0f);
        wallet.setCurrency("USD");

        // Act
        float updatedAmount = wallet.creditAmount(50.0f, Currency.USD);

        // Assert
        Assert.assertEquals(150.0f, updatedAmount, 0.01);
        Assert.assertEquals(150.0f, wallet.getAmount(), 0.01);
    }

    @Test
    public void testDebitAmount_SameCurrency() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setAmount(100.0f);
        wallet.setCurrency("USD");

        // Act
        float updatedAmount = wallet.debitAmount(50.0f, Currency.USD);

        // Assert
        Assert.assertEquals(50.0f, updatedAmount, 0.01);
        Assert.assertEquals(50.0f, wallet.getAmount(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDebitAmount_InsufficientFunds() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setAmount(100.0f);
        wallet.setCurrency("USD");

        // Act
        wallet.debitAmount(150.0f, Currency.USD);

    }

    // UPDATE WITH DIFFERENT METHODS !!!!!!!!

}
