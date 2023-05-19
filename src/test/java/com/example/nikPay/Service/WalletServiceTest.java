package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {
    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepo walletRepo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCredit_ValidData_CreditsAmount() {
        // Arrange
        String userID = "user-id";
        float amount = 100.0f;
        Currency currency = Currency.USD;

        Wallet wallet = new Wallet();
        wallet.setUserID(userID);
        wallet.setAmount(500.0f);
        wallet.setCurrency("USD");

        when(walletRepo.findByUserID(userID)).thenReturn(wallet);
        when(walletRepo.save(any(Wallet.class))).thenReturn(wallet);

        // Act
        float updatedAmount = walletService.credit(userID, amount, currency);

        // Assert
        assertEquals(600.0f, updatedAmount, 0.01);
        assertEquals(600.0f, wallet.getAmount(), 0.01);
        verify(walletRepo, times(1)).save(wallet);
    }

    @Test
    public void testDebit_ValidData_DebitsAmount() {
        // Arrange
        String userID = "user-id";
        float amount = 50.0f;
        Currency currency = Currency.USD;

        Wallet wallet = new Wallet();
        wallet.setUserID(userID);
        wallet.setAmount(200.0f);
        wallet.setCurrency("USD");

        when(walletRepo.findByUserID(userID)).thenReturn(wallet);
        when(walletRepo.save(any(Wallet.class))).thenReturn(wallet);

        // Act
        float updatedAmount = walletService.debit(userID, amount, currency);

        // Assert
        Assertions.assertEquals(150.0f, updatedAmount, 0.01);
        Assertions.assertEquals(150.0f, wallet.getAmount(), 0.01);
        verify(walletRepo, times(1)).findByUserID(userID);
    }

    @Test
    public void testDebit_InsufficientFunds_ThrowsException() {
        // Arrange
        String userID = "user-id";
        float amount = 300.0f;
        Currency currency = Currency.USD;

        Wallet wallet = new Wallet();
        wallet.setUserID(userID);
        wallet.setAmount(200.0f);
        wallet.setCurrency("USD");

        when(walletRepo.findByUserID(userID)).thenReturn(wallet);

        // Act and Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            walletService.debit(userID, amount, currency);
        });

        verify(walletRepo, times(1)).findByUserID(userID);
        verify(walletRepo, never()).save(any(Wallet.class));
    }
    @Test
    public void testDebit_WalletNotFound_ThrowsException() {
        // Arrange
        String userID = "user-id";
        float amount = 100.0f;
        Currency currency = Currency.USD;

        when(walletRepo.findByUserID(userID)).thenReturn(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.debit(userID, amount, currency);
        }, "Wallet not found for userID: " + userID);
    }
}
