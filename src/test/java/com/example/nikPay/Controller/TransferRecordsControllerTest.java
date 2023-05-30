package com.example.nikPay.Controller;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Service.TransferRecordService;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TransferRecordsControllerTest {

    @Mock
    private TransferRecordService transferRecordService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransferRecordsController transferRecordsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSenderTransactions_ValidUserId_Credit() {
        // Arrange
        String userId = "user1";
        TransactionType transactionType = TransactionType.CREDIT;
        List<TransferRecords> transactions = new ArrayList<>();
        when(userService.isValidUserId(userId)).thenReturn(true);
        when(transferRecordService.getSenderTransactions(userId)).thenReturn(transactions);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions(userId, transactionType);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, times(1)).getSenderTransactions(userId);
    }

    @Test
    public void testGetSenderTransactions_ValidUserId_Debit() {
        // Arrange
        String userId = "user1";
        TransactionType transactionType = TransactionType.DEBIT;
        List<TransferRecords> transactions = new ArrayList<>();
        when(userService.isValidUserId(userId)).thenReturn(true);
        when(transferRecordService.getReceiverTransactions(userId)).thenReturn(transactions);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions(userId, transactionType);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, times(1)).getReceiverTransactions(userId);
    }

    @Test
    public void testGetSenderTransactions_InvalidUserId() {
        // Arrange
        String userId = "invalidUser";
        TransactionType transactionType = TransactionType.CREDIT;
        when(userService.isValidUserId(userId)).thenReturn(false);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions(userId, transactionType);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, never()).getSenderTransactions(anyString());
        verify(transferRecordService, never()).getReceiverTransactions(anyString());
    }
}
