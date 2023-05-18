package com.example.nikPay.Service;

import com.example.nikPay.Currency;

import java.util.HashMap;
import java.util.Map;

public class CurrencyService {
    public static float convertCurrency(Currency baseCurrency, Currency targetCurrency, float value) {

        Map<String, Float> currencyRates = new HashMap<>();
        currencyRates.put("USD", 1f);
        currencyRates.put("INR", 82.75f);
        currencyRates.put("EURO", 0.93f);
        currencyRates.put("AUD", 1.51f);
        currencyRates.put("GBP", 0.81f);
        currencyRates.put("JPY", 138.44f);
        currencyRates.put("CAD", 1.35f);

        float baseCurrencyRate = currencyRates.get(baseCurrency.name());
        float targetCurrencyRate = currencyRates.get(targetCurrency.name());
        float ratio = targetCurrencyRate/baseCurrencyRate;
        return value * ratio;
    }

}
