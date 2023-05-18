package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Service.WalletService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private WalletController walletController;

//    @Test
//    public void testUpdateAmount_ValidToken_ReturnsUpdatedAmount() throws Exception {
//        float updatedAmount = 500.0f;
//        String token = "valid-token";
//
//        // Mock the behavior of JwtUtil.verifyToken() method
//        when(jwtUtil.verifyToken(token)).thenReturn(true);
//
//        // Mock the behavior of JwtUtil.parseToken() method
//        when(jwtUtil.parseToken(token)).thenReturn(getMockClaims());
//
//        // Mock the behavior of WalletService.credit() method
//        when(walletService.credit(anyString(), anyFloat())).thenReturn(updatedAmount);
//
//        // Setup MockMvc
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
//
//        // Perform the POST request and assert the response
//        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/credit/{amount}", 100.0f)
//                        .header("token", token))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(updatedAmount)));
//    }
//
//    @Test
//    public void testUpdateAmount_InvalidToken_ReturnsUnauthorizedAccess() throws Exception {
//        String token = "invalid-token";
//
//        // Mock the behavior of JwtUtil.verifyToken() method
//        when(jwtUtil.verifyToken(token)).thenReturn(false);
//
//        // Setup MockMvc
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
//
//        // Perform the POST request and assert the response
//        mockMvc.perform(MockMvcRequestBuilders.post("/wallet/credit/{amount}", 100.0f)
//                        .header("token", token))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
//    }
//
//    private Claims getMockClaims() {
//        // Create a mock Claims object with required data
//        Claims claims = Jwts.claims();
//        claims.setSubject("202d8aa3-89d2-4497-8880-97194fc8aa5c");
//        // Set any other required claims or data
//
//        return claims;
//    }

}
