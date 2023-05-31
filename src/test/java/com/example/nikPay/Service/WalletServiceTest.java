package com.example.nikPay.Service;
import com.example.nikPay.Enums.Currency;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private TransferRecordService transferRecordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletService(walletRepo, transferRecordService);

    }

    @Test
    public void testCreateWallet_ValidData_ReturnsCreatedWallet() {
        // Arrange
        String userID = "user1";
        String currency = "USD";
        Wallet wallet = new Wallet(userID, currency);
        when(walletRepo.save(wallet)).thenReturn(wallet);

        // Act
        Wallet createdWallet = walletService.createWallet(userID, currency);

        // Assert
        assertNotNull(createdWallet);
        assertEquals(userID, createdWallet.getUserID());
        assertEquals(currency, createdWallet.getCurrency());
        verify(walletRepo, times(1)).save(wallet);
    }

    @Test
    public void testGetTransactionIds_ExistingWalletId_ReturnsTransactionIds() {
        // Arrange
        Integer walletId = 1;
        Wallet wallet = new Wallet();
        wallet.setTransactionIds(Arrays.asList("1", "2", "3"));
        when(walletRepo.findById(String.valueOf(walletId))).thenReturn(Optional.of(wallet));

        // Act
        List<String> transactionIds = walletService.getTransactionIds(walletId);

        // Assert
        assertNotNull(transactionIds);
        assertEquals(3, transactionIds.size());
        assertEquals(Arrays.asList("1", "2", "3"), transactionIds);
        verify(walletRepo, times(1)).findById(String.valueOf(walletId));
    }

    @Test
    public void testGetTransactionIds_NonExistingWalletId_ReturnsNull() {
        // Arrange
        Integer walletId = 1;
        when(walletRepo.findById(String.valueOf(walletId))).thenReturn(Optional.empty());

        // Act
        List<String> transactionIds = walletService.getTransactionIds(walletId);

        // Assert
        assertNull(transactionIds);
        verify(walletRepo, times(1)).findById(String.valueOf(walletId));
    }

    @Test
    public void testCredit_ValidData_CreditsAmountAndSavesWallet() {
        // Arrange
        String userID = "user1";
        float amount = 100.0f;
        Currency currency = Currency.USD;
        Wallet wallet = new Wallet(userID, String.valueOf(currency));
        wallet.setAmount(500.0f);
        when(walletRepo.findByUserID(userID)).thenReturn(wallet);
        when(walletRepo.save(wallet)).thenReturn(wallet);

        // Act
        float updatedAmount = walletService.credit(userID, amount, currency);

        // Assert
        assertEquals(600.0f, updatedAmount, 0.01);
        assertEquals(600.0f, wallet.getAmount(), 0.01);
        verify(walletRepo, times(1)).save(wallet);
        verify(transferRecordService, times(1)).saveTransferRecordsInWallet("self", userID, updatedAmount, currency);
    }


    @Test
    public void testDebit_ValidData_DebitsAmountAndSavesWallet() {
        // Arrange
        String userID = "user1";
        float amount = 50.0f;
        Currency currency = Currency.USD;
        Wallet wallet = new Wallet(userID, String.valueOf(currency));
        wallet.setAmount(200.0f);
        when(walletRepo.findByUserID(userID)).thenReturn(wallet);
        when(walletRepo.save(wallet)).thenReturn(wallet);

        // Act
        float updatedAmount = walletService.debit(userID, amount, currency);

        // Assert
        assertEquals(150.0f, updatedAmount, 0.01);
        assertEquals(150.0f, wallet.getAmount(), 0.01);
        verify(walletRepo, times(1)).save(wallet);
        verify(transferRecordService, times(1)).saveTransferRecordsInWallet(userID, "self", updatedAmount, currency);
    }

    @Test
    public void testDebit_InsufficientFunds_ThrowsException() {
        // Arrange
        String userID = "user1";
        float amount = 300.0f;
        Currency currency = Currency.USD;
        Wallet wallet = new Wallet(userID, String.valueOf(currency));
        wallet.setAmount(200.0f);
        when(walletRepo.findByUserID(userID)).thenReturn(wallet);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.debit(userID, amount, currency);
        });

        verify(walletRepo, times(1)).findByUserID(userID);
        verify(walletRepo, never()).save(wallet);
        verifyNoInteractions(transferRecordService);
    }

    @Test
    public void testDebit_WalletNotFound_ThrowsException() {
        // Arrange
        String userID = "user1";
        float amount = 100.0f;
        Currency currency = Currency.USD;
        when(walletRepo.findByUserID(userID)).thenReturn(null);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.debit(userID, amount, currency);
        }, "Wallet not found for userID: " + userID);

        verify(walletRepo, times(1)).findByUserID(userID);
        verify(walletRepo, never()).save(any(Wallet.class));
        verifyNoInteractions(transferRecordService);
    }
}
