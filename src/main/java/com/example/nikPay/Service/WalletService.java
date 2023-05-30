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

    @Autowired
    private TransferRecordService transferRecordService;

    public float credit(String userID, float amount, Currency currency) {
        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet != null) {
            float updatedAmount =  wallet.creditAmount(amount, currency);
            walletRepo.save(wallet);

            return updatedAmount;
        } else {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
    }

    public float debit(String userID, float amount, Currency currency) {

        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet != null) {
            Currency userCurrency = Currency.valueOf(wallet.getCurrency());
            return wallet.debitAmount(amount, currency);
        } else {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
    }

    public void save(Wallet wallet) {
        walletRepo.save(wallet);
    }
}

