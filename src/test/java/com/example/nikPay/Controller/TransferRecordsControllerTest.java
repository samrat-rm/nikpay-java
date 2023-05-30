package com.example.nikPay.Controller;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Service.TransferRecordService;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.TransactionType;
import com.example.nikPay.Config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransferRecordsControllerTest {

    @Mock
    private TransferRecordService transferRecordService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransferRecordsController transferRecordsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetSenderTransactions_ValidToken_Transactions() {
        // Arrange
        String token = "validToken";
        String userId = "user1";
        List<TransferRecords> transactions = Arrays.asList(
                new TransferRecords(1, 100f, "sender1", "receiver1", "USD"),
                new TransferRecords(2, 200f, "sender1", "receiver2", "EUR")
        );
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserIDFromToken(token)).thenReturn(userId);
        when(userService.isValidUserId(userId)).thenReturn(true);
        when(transferRecordService.getTransactions(userId)).thenReturn(transactions);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(userService, times(1)).getUserIDFromToken(token);
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, times(1)).getTransactions(userId);
    }

    @Test
    public void testGetSenderTransactions_ValidToken_NoTransactions() {
        // Arrange
        String token = "validToken";
        String userId = "user1";
        List<TransferRecords> transactions = Collections.emptyList();
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserIDFromToken(token)).thenReturn(userId);
        when(userService.isValidUserId(userId)).thenReturn(true);
        when(transferRecordService.getTransactions(userId)).thenReturn(transactions);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions(token);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(userService, times(1)).getUserIDFromToken(token);
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, times(1)).getTransactions(userId);
    }


    @Test
    public void testGetSenderTransactions_InvalidToken() {
        // Arrange
        String token = "invalidToken";
        TransactionType transactionType = TransactionType.CREDIT;
        when(jwtUtil.verifyToken(token)).thenReturn(false);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions( token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(userService, never()).getUserIDFromToken(anyString());
        verify(userService, never()).isValidUserId(anyString());
        verify(transferRecordService, never()).getTransactions(anyString());
        verify(transferRecordService, never()).getTransactions(anyString());
    }
    @Test
    public void testGetSenderTransactions_InvalidUserId() {
        // Arrange
        String token = "validToken";
        TransactionType transactionType = TransactionType.CREDIT;
        String userId = "invalidUser";
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserIDFromToken(token)).thenReturn(userId);
        when(userService.isValidUserId(userId)).thenReturn(false);

        // Act
        ResponseEntity<List<TransferRecords>> response = transferRecordsController.getSenderTransactions( token);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(userService, times(1)).getUserIDFromToken(token);
        verify(userService, times(1)).isValidUserId(userId);
        verify(transferRecordService, never()).getTransactions(anyString());
        verify(transferRecordService, never()).getTransactions(anyString());
    }
}