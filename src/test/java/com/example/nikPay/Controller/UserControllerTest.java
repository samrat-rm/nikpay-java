package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Config.TokenResponse;
import com.example.nikPay.Currency;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.Model.User;
import com.example.nikPay.Service.WalletService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSaveUser_Success() throws Exception {
        // Arrange
        User user = new User("example@example.com", "password", "samrat", "r m");
        user.setUserID("1");

        when(userService.saveUser(any(User.class), eq(Currency.AUD))).thenReturn(user);
        when(jwtUtil.generateToken(eq("1"), anyLong())).thenReturn("mocked-token");

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("currency", "AUD")
                        .content("{\"email\":\"example@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mocked-token"));
    }

    @Test
    public void testGetUserById_UserExists_ReturnsUser() throws Exception {
        // Arrange
        String token = "valid-token";
        User user = new User("example@example.com", "password", "samrat", "r m");
        user.setUserID("1");

        when(jwtUtil.verifyToken(token)).thenReturn(true);
        when(userService.getUserFromToken(token)).thenReturn(user);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .header("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("example@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("password"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("samrat"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("r m"));
    }

    @Test
    public void testGetUserById_UserNotFound_ReturnsNotFoundStatus() throws Exception {
        when(userService.getUser("1")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testSignIn_ValidCredentials_ReturnsTrue() throws Exception {
        User user = new User("test@example.com", "password", "samrat", "r m");

        when(userService.checkPassword(user.getEmail(), user.getPassword())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    public void testSignIn_InvalidCredentials_ReturnsFalse() throws Exception {
        User user = new User("test1234@example.com", "password", "samrat", "r m");

        when(userService.checkPassword(user.getEmail(), user.getPassword())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test1234@example.com\",\"password\":\"password\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    private Claims getMockClaims() {
        Claims claims = Jwts.claims();
        claims.setSubject("202d8aa3-89d2-4497-8880-97194fc8aa5c");

        return claims;
    }
}
