package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Service.TransferService;
import com.example.nikPay.Service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransferControllerTest {

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private TransferService transferService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckBalance_ValidToken_ReturnsBalance() {
        // Arrange
        String token = "valid-token";
        String userID = "user-id";
        float balance = 1000f;

        Wallet wallet = new Wallet();
        wallet.setAmount(balance);
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        Claims claims = new DefaultClaims();
        claims.setSubject(userID);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(walletRepo.findByUserID(userID)).thenReturn(wallet);

        // Act
        ResponseEntity<Float> response = transferController.checkBalance(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balance, response.getBody(), 0.01);
    }

    @Test
    void testCheckBalance_InvalidToken_ReturnsUnauthorized() {
        // Arrange
        String token = "invalid-token";
        when(jwtUtil.verifyToken(token)).thenReturn(false);

        // Act
        ResponseEntity<Float> response = transferController.checkBalance(token);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testCheckBalance_WalletNotFound_ReturnsNotFound() {
        // Arrange
        String token = "valid-token";
        String userID = "user-id";
        when(jwtUtil.verifyToken(token)).thenReturn(true);
        Claims claims = new DefaultClaims();
        claims.setSubject(userID);
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(walletRepo.findByUserID(userID)).thenReturn(null);

        // Act
        ResponseEntity<Float> response = transferController.checkBalance(token);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testTransferMoney_ValidTransfer_ReturnsSuccessMessage() {
        // Arrange
        String token = "valid-token";
        String email = "recipient@example.com";
        float amount = 100f;

        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(transferService.transfer(token, email, amount)).thenReturn(true);

        // Act
        ResponseEntity<?> response = transferController.transferMoney(token, email, amount);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Money transferred successfully", response.getBody());

        // Verify that the transferService method was called with the correct arguments
        verify(transferService).transfer(token, email, amount);
    }

    @Test
    void testTransferMoney_InvalidTransfer_ReturnsBadRequest() {
        // Arrange
        String token = "valid-token";
        String email = "recipient@example.com";
        float amount = 100f;

        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(transferService.transfer(token, email, amount)).thenReturn(false);

        // Act
        ResponseEntity<?> response = transferController.transferMoney(token, email, amount);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid transfer", response.getBody());

        // Verify that the transferService method was called with the correct arguments
        verify(transferService).transfer(token, email, amount);
    }

    @Test
    void testTransferMoney_InvalidAmount_ReturnsBadRequest() {
        // Arrange
        String token = "valid-token";
        String email = "recipient@example.com";
        float amount = -100f;

        // Act
        ResponseEntity<?> response = transferController.transferMoney(token, email, amount);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Amount must be greater than zero and Email is required", response.getBody());

        // Verify that the transferService method was not called
        verifyNoInteractions(transferService);
    }

    @Test
    void testTransferMoney_MissingEmail_ReturnsBadRequest() {
        // Arrange
        String token = "valid-token";
        float amount = 100f;

        // Act
        ResponseEntity<?> response = transferController.transferMoney(token, "", amount);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Amount must be greater than zero and Email is required", response.getBody());

        // Verify that the transferService method was not called
        verifyNoInteractions(transferService);
    }

    @Test
    void testTransferMoney_Unauthorized_ReturnsUnauthorized() {
        // Arrange
        String token = "invalid-token";
        String email = "recipient@example.com";
        float amount = 100f;

        when(jwtUtil.verifyToken(token)).thenReturn(false);

        // Act
        ResponseEntity<?> response = transferController.transferMoney(token, email, amount);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        // Verify that the transferService method was not called
        verifyNoInteractions(transferService);
    }
}
