package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Currency;
import com.example.nikPay.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.nikPay.Service.WalletService;


@RestController
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/wallet/credit")
    public ResponseEntity<Float> creditAmount(@RequestParam("amount") float amount, @RequestParam("currency") Currency currency, @RequestHeader("token") String token) {
        try {
            if (!jwtUtil.verifyToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String userID = userService.getUserIDFromToken(token);
            float currentAmount = walletService.credit(userID, amount, currency);
            return ResponseEntity.ok(currentAmount);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/wallet/debit")
    public ResponseEntity<Float> debitAmount(@RequestParam("amount") float amount, @RequestParam("currency") Currency currency, @RequestHeader("token") String token) {
        try {
            if (!jwtUtil.verifyToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String userID = userService.getUserIDFromToken(token);
            float currentAmount = walletService.debit(userID, amount, currency);
            return ResponseEntity.ok(currentAmount);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
