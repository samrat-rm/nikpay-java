package com.example.nikPay.Enums;

public enum Currency {
    USD("United States Dollar"),
    EURO("Euro"),
    JPY("Japanese Yen"),
    GBP("British Pound Sterling"),
    AUD("Australian Dollar"),
    INR ("Indian Rupee"),
    CAD("Canadian Dollar");

    private final String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
