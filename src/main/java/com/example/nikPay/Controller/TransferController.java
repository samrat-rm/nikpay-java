package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Currency;
import com.example.nikPay.Model.User;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.WalletRepo;
import com.example.nikPay.Service.TransferService;
import com.example.nikPay.Service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.LinkedTransferQueue;

@RestController
public class TransferController {
    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private TransferService transferService;


    @GetMapping("/users/balance")
    public ResponseEntity<Float> checkBalance(@RequestHeader("token") String token) {
        String userID;
        if (!jwtUtil.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Claims claims = jwtUtil.parseToken(token);
        userID = claims.getSubject();
        Wallet wallet = walletRepo.findByUserID(userID);
        if (wallet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        float balance = wallet.getAmount();
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/users/transfer")
    public ResponseEntity<?> transferMoney(@RequestHeader("token") String token, @RequestParam("email") String email, @RequestParam("amount") float amount) {

        if (!jwtUtil.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            boolean isValidTransfer = transferService.transfer(token, email, amount);
            if (!isValidTransfer) {
                return ResponseEntity.badRequest().body("Invalid transfer");
            }
            return ResponseEntity.ok("Money transferred successfully");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
