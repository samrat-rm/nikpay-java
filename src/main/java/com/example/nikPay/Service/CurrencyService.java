package com.example.nikPay.Service;

import com.example.nikPay.Currency;

public class CurrencyService {
    public float convertCurrency(Currency baseCurrency, Currency targetCurrency, float value) {
        // Perform the currency conversion logic here
        // You can use a predefined exchange rate or an external API for currency conversion

        // For demonstration purposes, let's assume a conversion rate of 1.2 from EUR to USD
        float exchangeRate = 1.2f;

        if (baseCurrency == targetCurrency) {
            // No conversion needed, return the original value
            return value;
        } else if (baseCurrency == Currency.EUR && targetCurrency == Currency.USD) {
            // Convert from EUR to USD
            return value * exchangeRate;
        } else if (baseCurrency == Currency.USD && targetCurrency == Currency.EUR) {
            // Convert from USD to EUR
            return value / exchangeRate;
        } else {
            // Conversion between the given currencies is not supported
            throw new IllegalArgumentException("Currency conversion not supported for " + baseCurrency + " to " + targetCurrency);
        }
    }

}
