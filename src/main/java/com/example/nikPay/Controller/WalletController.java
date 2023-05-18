package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Model.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.nikPay.Service.WalletService;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WalletController {
    @Autowired
    private WalletService walletService ;
    @Autowired
    private JwtUtil jwtUtil ;
    @PostMapping("/wallet/credit/{amount}")
    public ResponseEntity<Float> updateAmount( @PathVariable("amount") float amount ,  @RequestHeader("token")String token){
        if (jwtUtil.verifyToken(token)) {
            Claims claims = jwtUtil.parseToken(token);
            String userID = claims.getSubject();
            float currentAmount = walletService.credit(userID,amount);
            return ResponseEntity.ok(currentAmount);
        }else {
            // Token verification failed, return unauthorized access response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
