package com.example.nikPay;

public enum Currency {
    USD("United States Dollar"),
    EUR("Euro"),
    JPY("Japanese Yen"),
    GBP("British Pound Sterling"),
    AUD("Australian Dollar"),
    CAD("Canadian Dollar");

    private final String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
