package com.example.nikPay.Service;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Config.MD5Hash;
import com.example.nikPay.Currency;
import com.example.nikPay.Repository.UserRepo;
import com.example.nikPay.Model.User;
import com.example.nikPay.Repository.WalletRepo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.nikPay.Model.*;
import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;


    public User getUser(String id){
        return userRepo.findByUserID(id);
    }
    public boolean checkPassword(String email, String password) {
        User user = userRepo.findByEmail(email);
        System.out.println("-------------------------------");
        String hashedPassword = MD5Hash.getMD5Hash(password);
        System.out.println(hashedPassword);
        return Objects.equals(user.getPassword(),hashedPassword );
    }
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User getUserFromToken(String token) {
        Claims claims = jwtUtil.parseToken(token);
        String userID = claims.getSubject();
        if (userID != null) {
            return userRepo.findByUserID(userID);
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public String getUserIDFromToken(String token) {
        Claims claims = jwtUtil.parseToken(token);
        String userID = claims.getSubject();
        if (userID != null) {
            return userID;
        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public User saveUser(User user , Currency currency){
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        Wallet wallet = new Wallet(user.getUserID(), currency.name() );
        Wallet wal = walletRepo.save(wallet);
        String hashedPassword = MD5Hash.getMD5Hash(user.getPassword());
        user.updateToHashPassword(hashedPassword);
        return userRepo.save(user);
    }
}
