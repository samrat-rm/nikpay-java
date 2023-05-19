package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Service.TransferService;
import com.example.nikPay.Service.UserService;
import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransferControllerTest {
    @Mock
    private WalletRepo walletRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @Before
    public void setup() {
        Wallet wallet = new Wallet("123", "USD");
        wallet.setAmount(1000f);
        when(walletRepo.findByUserID(eq("123"))).thenReturn(wallet);

        when(jwtUtil.verifyToken(anyString())).thenReturn(true);

        User user = new User("example@example.com", "password", "John", "Doe");
        when(userService.getUserByEmail(eq("example@example.com"))).thenReturn(user);

        when(transferService.transfer(anyString(), anyString(), anyFloat())).thenReturn(true);
    }

    @Test
    public void testCheckBalance_ValidToken_ReturnsBalance() {
        // Arrange
        String validToken = "valid-token";
        Claims mockClaims = Mockito.mock(Claims.class);
        when(jwtUtil.parseToken(validToken)).thenReturn(mockClaims);
        when(mockClaims.getSubject()).thenReturn("123");

        // Act
        ResponseEntity<Float> response = transferController.checkBalance(validToken);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(1000f, response.getBody(), 0.01);
    }

    @Test
    public void testTransferMoney_ValidInputs_ReturnsSuccessMessage() {
        // Arrange
        String validToken = "valid-token";
        String receiverEmail = "example@example.com";
        float amount = 500f;

        // Act
        ResponseEntity<?> response = transferController.transferMoney(validToken, receiverEmail, amount);

        // Assert
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Money transferred successfully", response.getBody());
        Mockito.verify(transferService, Mockito.times(1)).transfer(eq(validToken), eq(receiverEmail), eq(amount));
    }

    @Test
    public void testTransferMoney_InvalidAmount_ReturnsBadRequest() {
        // Arrange
        String validToken = "valid-token";
        String receiverEmail = "example@example.com";
        float invalidAmount = 0f;

        // Act
        ResponseEntity<?> response = transferController.transferMoney(validToken, receiverEmail, invalidAmount);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Amount must be greater than zero and Email is required", response.getBody());
        Mockito.verify(transferService, Mockito.never()).transfer(anyString(), anyString(), anyFloat());
    }

}
