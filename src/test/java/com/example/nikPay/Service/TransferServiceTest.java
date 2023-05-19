package com.example.nikPay.Service;

import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Repository.WalletRepo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransferServiceTest {
    @InjectMocks
    private TransferService transferService;

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testTransfer_ValidData_SuccessfulTransfer() {
        // Arrange
        String token = "valid-token";
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        float amount = 100.0f;

        User sender = new User();
        sender.setUserID("sender-id");

        User receiver = new User();
        receiver.setUserID("receiver-id");

        Wallet senderWallet = new Wallet();
        senderWallet.setUserID("sender-id");
        senderWallet.setAmount(200.0f);
        senderWallet.setCurrency("USD");

        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID("receiver-id");
        receiverWallet.setAmount(100.0f);
        receiverWallet.setCurrency("USD");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(receiverEmail)).thenReturn(receiver);
        when(walletRepo.findByUserID("sender-id")).thenReturn(senderWallet);
        when(walletRepo.findByUserID("receiver-id")).thenReturn(receiverWallet);

        // Act
        boolean result = transferService.transfer(token, receiverEmail, amount);

        // Assert
        Assert.assertTrue(result);
        Assert.assertEquals(100.0f, senderWallet.getAmount(), 0.01);
        Assert.assertEquals(200.0f, receiverWallet.getAmount(), 0.01);
        Mockito.verify(walletRepo, Mockito.times(2)).save(any(Wallet.class));
    }



    @Test
    public void testTransfer_InvalidToken_ThrowsException() {
        // Arrange
        String token = "invalid-token";
        String receiverEmail = "receiver@example.com";
        float amount = 100.0f;

        when(userService.getUserFromToken(token)).thenReturn(null);

        // Act and Assert
        try {
            transferService.transfer(token, receiverEmail, amount);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            Assert.assertEquals("Invalid token", e.getMessage());
        }
    }

    @Test
    public void testTransfer_ReceiverNotFound_ThrowsException() {
        // Arrange
        String token = "valid-token";
        String receiverEmail = "receiver@example.com";
        float amount = 100.0f;

        User sender = new User();
        sender.setUserID("sender-id");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(receiverEmail)).thenReturn(null);

        // Act and Assert
        try {
            transferService.transfer(token, receiverEmail, amount);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            Assert.assertEquals("Receiver not found with email: receiver@example.com", e.getMessage());
        }
    }

    @Test
    public void testTransfer_InsufficientFunds_ThrowsException() {
        // Arrange
        String token = "valid-token";
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        float amount = 1000.0f;

        User sender = new User();
        sender.setUserID("sender-id");

        User receiver = new User();
        receiver.setUserID("receiver-id");

        Wallet senderWallet = new Wallet();
        senderWallet.setUserID("sender-id");
        senderWallet.setAmount(200.0f);
        senderWallet.setCurrency("USD");

        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID("receiver-id");
        receiverWallet.setAmount(100.0f);
        receiverWallet.setCurrency("USD");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(receiverEmail)).thenReturn(receiver);
        when(walletRepo.findByUserID("sender-id")).thenReturn(senderWallet);
        when(walletRepo.findByUserID("receiver-id")).thenReturn(receiverWallet);

        // Act and Assert
        try {
            transferService.transfer(token, receiverEmail, amount);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Assert
            Assert.assertEquals("Insufficient funds in sender's wallet", e.getMessage());
        }
    }


    @Test
    public void testCheckBalance_SufficientBalance_ReturnsTrue() {
        // Arrange
        String userID = "user-id";
        float debitAmount = 50.0f;

        Wallet wallet = new Wallet();
        wallet.setUserID(userID);
        wallet.setAmount(100.0f);

        when(walletRepo.findByUserID(userID)).thenReturn(wallet);

        // Act
        boolean result = transferService.checkBalance(userID, debitAmount);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckBalance_InsufficientBalance_ReturnsFalse() {
        // Arrange
        String userID = "user-id";
        float debitAmount = 150.0f;

        Wallet wallet = new Wallet();
        wallet.setUserID(userID);
        wallet.setAmount(100.0f);

        when(walletRepo.findByUserID(userID)).thenReturn(wallet);

        // Act
        boolean result = transferService.checkBalance(userID, debitAmount);

        // Assert
        Assert.assertFalse(result);
    }
}
