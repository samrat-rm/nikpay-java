package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Model.User;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Model.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser_ValidUser_ReturnsSavedUser() {
        // Mock the walletRepo.save() method
        when(walletRepo.save(any(Wallet.class))).thenReturn(new Wallet("walletId" , Currency.AUD.name()));

        // Mock the userRepo.save() method
        User savedUser = new User("example@example.com", "password" , "samrat","r m");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // Call the saveUser() method
        User user = new User("example@example.com", "password" , "samrat","r m");
        User result = userService.saveUser(user , Currency.AUD);

        // Verify the walletRepo.save() method is called once
        verify(walletRepo, times(1)).save(any(Wallet.class));

        // Verify the userRepo.save() method is called once
        verify(userRepo, times(1)).save(any(User.class));

        // Assert the result is the same as the saved user
        Assertions.assertEquals(savedUser, result);
    }

    @Test
    void testSaveUser_NullEmail_ThrowsIllegalArgumentException() {
        // Call the saveUser() method with a user having null email
        User user = new User(null, "password",  "samrat","r m");

        // Assert that an IllegalArgumentException is thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user , Currency.AUD));

        // Verify that walletRepo.save() method is not called
        verify(walletRepo, never()).save(any(Wallet.class));

        // Verify that userRepo.save() method is not called
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void testSaveUser_NullPassword_ThrowsIllegalArgumentException() {
        // Call the saveUser() method with a user having null password
        User user = new User("example@example.com", null,  "samrat","r m");

        // Assert that an IllegalArgumentException is thrown
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user ,Currency.AUD));

        // Verify that walletRepo.save() method is not called
        verify(walletRepo, never()).save(any(Wallet.class));

        // Verify that userRepo.save() method is not called
        verify(userRepo, never()).save(any(User.class));
    }
}
