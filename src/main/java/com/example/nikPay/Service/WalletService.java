package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.nikPay.Service.CurrencyService.convertCurrency;


@Service
public class WalletService {
    @Autowired
    private WalletRepo walletRepo;
    public float credit(String userID, float amount, Currency currency) {
        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet != null) {

            Currency userCurrency =  Currency.valueOf(wallet.getCurrency());;

            if (userCurrency == currency) {
                // Same currency, directly add the amount to the wallet
                float previousAmount = wallet.getAmount();
                float updatedAmount = previousAmount + amount;
                wallet.setAmount(updatedAmount);
                walletRepo.save(wallet);
                return wallet.getAmount();
            } else {
                // Convert the received currency to the user's currency
                float convertedAmount = convertCurrency(currency, userCurrency, amount);

                // Add the converted amount to the wallet
                float previousAmount = wallet.getAmount();
                float updatedAmount = previousAmount + convertedAmount;
                wallet.setAmount(updatedAmount);
                walletRepo.save(wallet);
                return wallet.getAmount();
            }
        } else {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
    }

    public float debit(String userID, float amount) {
        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet != null) {
            float previousAmount = wallet.getAmount();
            float updatedAmount = previousAmount - amount;
            if (updatedAmount < 0) {
                throw new IllegalArgumentException("Insufficient funds. Cannot debit amount: " + amount);
            }
            wallet.setAmount(updatedAmount);
            walletRepo.save(wallet);
            return wallet.getAmount();
        } else {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
    }
    public void save(Wallet wallet){
        walletRepo.save(wallet);
    }
}

