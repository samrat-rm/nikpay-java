package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.TransferRecordsRepo;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Service.TransferRecordService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;

public class TransferRecordServiceTest {


    @Mock
    private TransferRecordsRepo transferRecordsRepo;

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private TransferRecordService transferRecordService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testSaveTransferRecord_Success() {
        // Arrange
        String senderId = "senderId";
        String receiverId = "receiverId";
        float amount = 100.0f;
        Currency currency = Currency.USD;

        TransferRecords transferRecord = new TransferRecords(amount, senderId, receiverId);
        transferRecord.setCurrency(String.valueOf(currency));
        TransferRecords savedRecord = new TransferRecords();
        savedRecord.setId(1);
        Mockito.when(transferRecordsRepo.save(Mockito.any(TransferRecords.class))).thenReturn(savedRecord);

        // Act
        String transactionId = transferRecordService.saveTransferRecord(senderId, receiverId, amount, currency);

        // Assert
        Assertions.assertEquals("1", transactionId);
        Mockito.verify(transferRecordsRepo, Mockito.times(1)).save(Mockito.any(TransferRecords.class));
    }

    @Test
    public void testSaveTransferRecordsInWallet_Success() {
        // Arrange
        String senderId = "senderId";
        String receiverId = "receiverId";
        float amount = 100.0f;
        Currency senderCurrency = Currency.USD;
        String transactionId = "1";

        Wallet senderWallet = new Wallet();
        senderWallet.setUserID(senderId);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID(receiverId);
        Mockito.when(walletRepo.findByUserID(senderId)).thenReturn(senderWallet);
        Mockito.when(walletRepo.findByUserID(receiverId)).thenReturn(receiverWallet);

        TransferRecords savedRecord = new TransferRecords();
        savedRecord.setId(1);
        Mockito.when(transferRecordsRepo.save(Mockito.any(TransferRecords.class))).thenReturn(savedRecord);

        // Act
        transferRecordService.saveTransferRecordsInWallet(senderId, receiverId, amount, senderCurrency);

        // Assert
        Assertions.assertEquals(1, senderWallet.getTransactionIds().size());
        Assertions.assertEquals(1, receiverWallet.getTransactionIds().size());
        Assertions.assertTrue(senderWallet.getTransactionIds().contains(transactionId));
        Assertions.assertTrue(receiverWallet.getTransactionIds().contains(transactionId));
    }

    @Test
    public void testSaveTransferRecordsInWallet_Failure() {
        // Arrange
        String senderId = "senderId";
        String receiverId = "receiverId";
        float amount = 100.0f;
        Currency senderCurrency = Currency.USD;

        Wallet senderWallet = new Wallet();
        senderWallet.setUserID(senderId);
        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserID(receiverId);
        Mockito.when(walletRepo.findByUserID(senderId)).thenReturn(senderWallet);
        Mockito.when(walletRepo.findByUserID(receiverId)).thenReturn(receiverWallet);

        Mockito.when(transferRecordsRepo.save(Mockito.any(TransferRecords.class))).thenReturn(null); // Simulate saving failure

        // Act and Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            transferRecordService.saveTransferRecordsInWallet(senderId, receiverId, amount, senderCurrency);
        });

        // Verify that no transaction IDs were added to the wallets
        Assertions.assertEquals(0, senderWallet.getTransactionIds().size());
        Assertions.assertEquals(0, receiverWallet.getTransactionIds().size());
    }


}
