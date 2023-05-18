package com.example.nikPay.Service;

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
    private UserRepo userRepo;

    public User getUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public boolean checkBalance(UUID userID , float debit ){

    }
    public User saveUser(User user){
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        Wallet wallet = new Wallet(user.getUserID());
        Wallet wal = walletRepo.save(wallet);
        System.out.println(wal);
        return userRepo.save(user);
    }
}
