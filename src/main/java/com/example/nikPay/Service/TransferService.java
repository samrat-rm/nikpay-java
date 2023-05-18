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

    public boolean checkBalance(String userID , float debitAmount ){
        Wallet wallet  = walletRepo.findByUserID(userID);
        float balance = wallet.getAmount();
        if( balance > debitAmount){
            return true;
        }else{
            return false;
        }
    }
   public boolean transfer (String senderID , String receiverID , float amount ){
    return  true;
   }
}
