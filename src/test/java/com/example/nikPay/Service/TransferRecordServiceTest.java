package com.example.nikPay.Service;

import com.example.nikPay.Enums.Currency;
import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.TransferRecordsRepo;
import com.example.nikPay.Repository.WalletRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransferRecordServiceTest {

    @Mock
    private TransferRecordsRepo transferRecordsRepo;

    @Mock
    private WalletRepo walletRepo;

    @InjectMocks
    private TransferRecordService transferRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testSaveTransferRecord_Success() {
        // Arrange
        String senderId = "senderId";
        String receiverId = "receiverId";
        float amount = 100.0f;
        Currency currency = Currency.USD;

        TransferRecords transferRecord = new TransferRecords(amount, senderId, receiverId, String.valueOf(currency));
        transferRecord.setCurrency(String.valueOf(currency));
        TransferRecords savedRecord = new TransferRecords();
        savedRecord.setId(1);
        Mockito.doReturn(savedRecord).when(transferRecordsRepo).save(Mockito.any(TransferRecords.class));

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

}