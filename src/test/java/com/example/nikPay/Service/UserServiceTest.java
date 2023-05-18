package com.example.nikPay.Service;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Currency;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Model.User;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Model.Wallet;
import io.jsonwebtoken.Claims;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private WalletRepo walletRepo;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_ValidUser_ReturnsSavedUser() {
        when(walletRepo.save(any(Wallet.class))).thenReturn(new Wallet("walletId" , Currency.AUD.name()));

        User savedUser = new User("example@example.com", "password" , "samrat","r m");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User user = new User("example@example.com", "password" , "samrat","r m");
        User result = userService.saveUser(user , Currency.AUD);

        verify(walletRepo, times(1)).save(any(Wallet.class));

        verify(userRepo, times(1)).save(any(User.class));

        Assertions.assertEquals(savedUser, result);
    }

    @Test
    void testSaveUser_NullEmail_ThrowsIllegalArgumentException() {
        User user = new User(null, "password",  "samrat","r m");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user , Currency.AUD));

        verify(walletRepo, never()).save(any(Wallet.class));

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testSaveUser_NullPassword_ThrowsIllegalArgumentException() {
        User user = new User("example@example.com", null,  "samrat","r m");

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user ,Currency.AUD));

        verify(walletRepo, never()).save(any(Wallet.class));

        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void testGetUser_ValidId_ReturnsUser() {
        // Arrange
        String userId = "1";
        User user = new User();
        user.setUserID(userId);
        when(userRepo.findByUserID(userId)).thenReturn(user);

        // Act
        User result = userService.getUser(userId);

        // Assert
        Assert.assertEquals(user, result);
    }


    @Test
    public void testCheckPassword_ValidCredentials_ReturnsTrue() {
        // Arrange
        String email = "example@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        when(userRepo.findByEmail(email)).thenReturn(user);

        // Act
        boolean result = userService.checkPassword(email, password);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testGetUserByEmail_ValidEmail_ReturnsUser() {
        // Arrange
        String email = "example@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepo.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        Assert.assertEquals(user, result);
    }

    @Test
    public void testGetUserFromToken_ValidToken_ReturnsUser() {
        // Arrange
        String token = "valid-token";
        Claims claims = Mockito.mock(Claims.class);
        String userId = "1";
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(userId);
        User user = new User();
        user.setUserID(userId); // Corrected method name
        when(userRepo.findByUserID(userId)).thenReturn(user);

        // Act
        User result = userService.getUserFromToken(token);

        // Assert
        Assert.assertEquals(user, result);
    }


    @Test
    public void testGetUserIDFromToken_ValidToken_ReturnsUserID() {
        // Arrange
        String token = "valid-token";
        Claims claims = Mockito.mock(Claims.class);
        String userId = "1";
        when(jwtUtil.parseToken(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(userId);

        // Act
        String result = userService.getUserIDFromToken(token);

        // Assert
        Assert.assertEquals(userId, result);
    }

}
