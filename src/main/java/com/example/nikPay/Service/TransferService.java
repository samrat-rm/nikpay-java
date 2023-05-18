package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Model.User;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nikPay.Model.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class TransferService {
    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    public User getUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public boolean transfer(String token, String receiverEmail, float amount) {
        // Find sender based on token
        User sender = userService.getUserFromToken(token);
        if (sender == null) {
            throw new IllegalArgumentException("Invalid token");
        }

        // Find receiver based on email
        User receiver = userService.getUserByEmail(receiverEmail);
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver not found with email: " + receiverEmail);
        }

        // Get sender's wallet
        Wallet senderWallet = walletRepo.findByUserID(sender.getUserID());
        if (senderWallet == null) {
            throw new IllegalArgumentException("Sender wallet not found for userID: " + sender.getUserID());
        }

        // Get receiver's wallet
        Wallet receiverWallet = walletRepo.findByUserID(receiver.getUserID());
        if (receiverWallet == null) {
            throw new IllegalArgumentException("Receiver wallet not found for userID: " + receiver.getUserID());
        }

        // Check if sender has sufficient balance
        if (senderWallet.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient funds in sender's wallet");
        }

        Currency senderCurrency = Currency.valueOf(senderWallet.getCurrency());

        // Debit amount from sender
        senderWallet.debitAmount(amount, senderCurrency);
        walletRepo.save(senderWallet);

        // Credit amount to receiver
        receiverWallet.creditAmount(amount, senderCurrency);
        walletRepo.save(receiverWallet);

        return true;
    }

    public boolean checkBalance(String userID , float debitAmount ){
        Wallet wallet  = walletRepo.findByUserID(userID);
        float balance = wallet.getAmount();
        if( balance > debitAmount){
            return true;
        }else{
            return false;
        }
    }

}
