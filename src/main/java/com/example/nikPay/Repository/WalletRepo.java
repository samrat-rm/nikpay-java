package com.example.nikPay.Repository;

import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepo extends JpaRepository <Wallet, Integer> {
    Wallet findByUserID(String userID);
}

