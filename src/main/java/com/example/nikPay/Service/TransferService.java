package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Model.User;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nikPay.Model.*;

@Service
public class TransferService {
    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private UserRepo userRepo;

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public boolean transfer(String token, String receiverEmail, float amount) {
        User sender = userService.getUserFromToken(token);
        if (sender == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        User receiver = userService.getUserByEmail(receiverEmail);
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found with email: " + receiverEmail);
        }

        Wallet senderWallet = walletRepo.findByUserID(sender.getUserID());
        if (senderWallet == null) {
            throw new IllegalArgumentException("Sender wallet not found for userID: " + sender.getUserID());
        }

        Wallet receiverWallet = walletRepo.findByUserID(receiver.getUserID());
        if (receiverWallet == null) {
            throw new IllegalArgumentException("Receiver wallet not found for userID: " + receiver.getUserID());
        }

        if (senderWallet.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient funds in sender's wallet");
        }

        Currency senderCurrency = Currency.valueOf(senderWallet.getCurrency());

        senderWallet.debitAmount(amount, senderCurrency);
        walletRepo.save(senderWallet);

        receiverWallet.creditAmount(amount, senderCurrency);
        walletRepo.save(receiverWallet);
        transferRecordService.saveTransferRecord(sender.getUserID(), receiver.getUserID(), amount, senderCurrency);

        return true;
    }


    public boolean checkBalance(String userID, float debitAmount) {
        Wallet wallet = walletRepo.findByUserID(userID);
        float balance = wallet.getAmount();
        return balance > debitAmount;
    }

}
