package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    @Test
    public void testConvertCurrency_SameCurrency() {
        // Arrange
        Currency baseCurrency = Currency.USD;
        Currency targetCurrency = Currency.USD;
        float value = 100f;

        // Act
        float convertedValue = CurrencyService.convertCurrency(baseCurrency, targetCurrency, value);

        // Assert
        assertEquals(value, convertedValue);
    }

    @Test
    public void testConvertCurrency_DifferentCurrency() {
        // Arrange
        Currency baseCurrency = Currency.USD;
        Currency targetCurrency = Currency.INR;
        float value = 100f;
        float expectedConvertedValue = 8275f;

        // Act
        float convertedValue = CurrencyService.convertCurrency(baseCurrency, targetCurrency, value);

        // Assert
        assertEquals(expectedConvertedValue, convertedValue);
    }

}
