package com.example.nikPay.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        User user = new User();
        user.setEmail("example@example.com");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserID("12345");

        // Assert
        Assertions.assertEquals("example@example.com", user.getEmail());
        Assertions.assertEquals("password", user.getPassword());
        Assertions.assertEquals("John", user.getFirstName());
        Assertions.assertEquals("Doe", user.getLastName());
        Assertions.assertEquals("12345", user.getUserID());
    }

    @Test
    public void testConstructor() {
        // Arrange
        String email = "example@example.com";
        String password = "password";
        String firstName = "John";
        String lastName = "Doe";

        // Act
        User user = new User(email, password, firstName, lastName);

        // Assert
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertNotNull(user.getUserID());
    }
}
