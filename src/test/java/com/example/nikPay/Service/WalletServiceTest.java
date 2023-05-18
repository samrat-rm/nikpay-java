package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WalletServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testCredit_ValidUserID_ReturnsUpdatedAmount() {
//        // Arrange
//        String userID = "123";
//        float previousAmount = 100.0f;
//        float amount = 50.0f;
//        float expectedUpdatedAmount = previousAmount + amount;
//
//        Wallet wallet = new Wallet(userID);
//        wallet.setAmount(previousAmount);
//
//        when(walletRepo.findByUserID(userID)).thenReturn(wallet);
//        when(walletRepo.save(any(Wallet.class))).thenReturn(wallet);
//
//        // Act
//        float result = walletService.credit(userID, amount);
//
//        // Assert
//        assertEquals(expectedUpdatedAmount, result);
//        assertEquals(expectedUpdatedAmount, wallet.getAmount());
//        verify(walletRepo, times(1)).findByUserID(userID);
//        verify(walletRepo, times(1)).save(wallet);
//    }
//
//    @Test
//    void testCredit_InvalidUserID_ThrowsIllegalArgumentException() {
//        // Arrange
//        String userID = "123";
//        float amount = 50.0f;
//
//        when(walletRepo.findByUserID(userID)).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> {
//            walletService.credit(userID, amount);
//        });
//
//        verify(walletRepo, times(1)).findByUserID(userID);
//        verify(walletRepo, never()).save(any(Wallet.class));
//    }
//
//    @Test
//    void testSave_ValidWallet_CallsWalletRepoSave() {
//        // Arrange
//        Wallet wallet = new Wallet("123" , Currency.AUD);
//
//        // Act
//        walletService.save(wallet);
//
//        // Assert
//        verify(walletRepo, times(1)).save(wallet);
//    }
}
