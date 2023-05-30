package com.example.nikPay.Repository;

import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepo extends JpaRepository <Wallet, String> {
     Wallet findByUserID(String userID);

}

