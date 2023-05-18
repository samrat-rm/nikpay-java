package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Controller.WalletController;
import com.example.nikPay.Currency;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.Service.WalletService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {
    @Mock
    private WalletService walletService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testCreditAmount_Success() throws Exception {
        // Arrange
        float amount = 100.0f;
        Currency currency = Currency.USD;
        String token = "valid-token";
        String userID = "1";
        float currentAmount = 200.0f;

        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserIDFromToken(token)).thenReturn(userID);
        when(walletService.credit(userID, amount, currency)).thenReturn(currentAmount);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/credit")
                        .header("token", token)
                        .param("amount", String.valueOf(amount))
                        .param("currency", currency.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(currentAmount)));
    }

    @Test
    public void testDebitAmount_Success() throws Exception {
        // Arrange
        float amount = 50.0f;
        Currency currency = Currency.USD;
        String token = "valid-token";
        String userID = "1";
        float currentAmount = 150.0f;

        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserIDFromToken(token)).thenReturn(userID);
        when(walletService.debit(userID, amount, currency)).thenReturn(currentAmount);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/debit")
                        .header("token", token)
                        .param("amount", String.valueOf(amount))
                        .param("currency", currency.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(currentAmount)));
    }

    @Test
    public void testCreditAmount_InvalidToken_ReturnsUnauthorized() throws Exception {
        // Arrange
        float amount = 100.0f;
        Currency currency = Currency.USD;
        String invalidToken = "invalid-token";

        when(jwtUtil.verifyToken(invalidToken)).thenReturn(false);

        // Act
        ResponseEntity<Float> response = walletController.creditAmount(amount, currency, invalidToken);

        // Assert
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testDebitAmount_InsufficientFunds_ReturnsBadRequest() throws Exception {
        // Arrange
        float amount = 500.0f;
        Currency currency = Currency.USD;
        String validToken = "valid-token";
        String userID = "123";

        when(jwtUtil.verifyToken(validToken)).thenReturn(true);
        when(userService.getUserIDFromToken(validToken)).thenReturn(userID);
        when(walletService.debit(userID, amount, currency)).thenThrow(new IllegalArgumentException("Insufficient funds"));

        // Act
        ResponseEntity<Float> response = walletController.debitAmount(amount, currency, validToken);

        // Assert
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}

