package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.nikPay.Service.CurrencyService.convertCurrency;


@Service
public class WalletService {
    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private TransferRecordService transferRecordService;

    @Autowired
    public WalletService(WalletRepo walletRepo, TransferRecordService transferRecordService) {
        this.walletRepo = walletRepo;
        this.transferRecordService = transferRecordService;
    }

    public Wallet createWallet(String userID, String currency) {
        Wallet wallet = Wallet.builder()
                .userID(userID)
                .currency(currency)
                .build();

        return walletRepo.save(wallet);
    }

    public List<String> getTransactionIds(Integer walletId) {

        Wallet wallet = walletRepo.findById(String.valueOf(walletId)).orElse(null);
        if (wallet != null) {
            return wallet.getTransactionIds();
        }
        return null;
    }

    public float credit(String userID, float amount, Currency currency) {

        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
        Currency userCurrency = Currency.valueOf(wallet.getCurrency());
        float updatedAmount = wallet.creditAmount(amount, currency);
        walletRepo.save(wallet);
        transferRecordService.saveTransferRecordsInWallet("self", userID, updatedAmount, userCurrency);
        return updatedAmount;

    }

    public float debit(String userID, float amount, Currency currency) {

        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found for userID: " + userID);
        }
        Currency userCurrency = Currency.valueOf(wallet.getCurrency());
        float updatedAmount =  wallet.debitAmount(amount, currency);
        walletRepo.save(wallet);
        transferRecordService.saveTransferRecordsInWallet(userID, "self", updatedAmount, userCurrency);
        return updatedAmount;

    }

    public void save(Wallet wallet) {
        walletRepo.save(wallet);
    }
}

