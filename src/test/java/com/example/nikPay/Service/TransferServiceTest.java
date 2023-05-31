package com.example.nikPay.Service;
import com.example.nikPay.Enums.Currency;
import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Repository.WalletRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransferServiceTest {
    @Mock
    private WalletRepo walletRepo;

    @Mock
    private UserService userService;

    @Mock
    private TransferRecordService transferRecordService;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransfer_ValidTransfer_Successful() {
        // Arrange
        String token = "validToken";
        String senderEmail = "sender@example.com";
        String receiverEmail = "receiver@example.com";
        float amount = 100.0f;

        User sender = new User();
        sender.setUserID("sender123");
        User receiver = new User();
        receiver.setUserID("receiver123");
        Wallet senderWallet = new Wallet();
        senderWallet.setUserID(sender.getUserID());
        senderWallet.setAmount(500.0f);
        senderWallet.setCurrency("USD");
        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID(receiver.getUserID());
        receiverWallet.setAmount(200.0f);
        receiverWallet.setCurrency("USD");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(receiverEmail)).thenReturn(receiver);
        when(walletRepo.findByUserID(sender.getUserID())).thenReturn(senderWallet);
        when(walletRepo.findByUserID(receiver.getUserID())).thenReturn(receiverWallet);

        // Act
        boolean transferSuccessful = transferService.transfer(token, receiverEmail, amount);

        // Assert
        assertTrue(transferSuccessful);
        assertEquals(400.0f, senderWallet.getAmount());
        assertEquals(300.0f, receiverWallet.getAmount());
        verify(walletRepo, times(1)).save(senderWallet);
        verify(walletRepo, times(1)).save(receiverWallet);
        verify(transferRecordService, times(1)).saveTransferRecord(sender.getUserID(), receiver.getUserID(), amount, Currency.USD);
    }

    @Test
    public void testTransfer_InvalidSenderToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalidToken";
        String receiverEmail = "receiver@example.com";
        float amount = 100.0f;

        when(userService.getUserFromToken(invalidToken)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(invalidToken, receiverEmail, amount));
        verify(userService, times(1)).getUserFromToken(invalidToken);
        verify(walletRepo, never()).save(any(Wallet.class));
        verify(transferRecordService, never()).saveTransferRecord(anyString(), anyString(), anyFloat(), any(Currency.class));
    }


    @Test
    public void testTransfer_InvalidReceiverEmail_ThrowsException() {
        // Arrange
        String token = "validToken";
        String invalidReceiverEmail = "invalid@example.com";
        float amount = 100.0f;

        User sender = new User();
        sender.setUserID("sender123");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(invalidReceiverEmail)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(token, invalidReceiverEmail, amount));
        verify(userService, times(1)).getUserFromToken(token);
        verify(userService, times(1)).getUserByEmail(invalidReceiverEmail);
        verifyNoInteractions(walletRepo, transferRecordService);
    }

    @Test
    public void testTransfer_InsufficientFunds_ThrowsException() {
        // Arrange
        String token = "validToken";
        String receiverEmail = "receiver@example.com";
        float amount = 1000.0f;

        User sender = new User();
        sender.setUserID("sender123");
        User receiver = new User();
        receiver.setUserID("receiver123");
        Wallet senderWallet = new Wallet();
        senderWallet.setUserID(sender.getUserID());
        senderWallet.setAmount(500.0f);
        senderWallet.setCurrency("USD");
        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID(receiver.getUserID());
        receiverWallet.setAmount(200.0f);
        receiverWallet.setCurrency("USD");

        when(userService.getUserFromToken(token)).thenReturn(sender);
        when(userService.getUserByEmail(receiverEmail)).thenReturn(receiver);
        when(walletRepo.findByUserID(sender.getUserID())).thenReturn(senderWallet);
        when(walletRepo.findByUserID(receiver.getUserID())).thenReturn(receiverWallet);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(token, receiverEmail, amount));
        verify(userService, times(1)).getUserFromToken(token);
        verify(userService, times(1)).getUserByEmail(receiverEmail);
        verify(walletRepo, times(1)).findByUserID(sender.getUserID());
        verify(walletRepo, times(1)).findByUserID(receiver.getUserID());
        verifyNoMoreInteractions(walletRepo, transferRecordService);
    }

}
