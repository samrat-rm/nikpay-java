package com.example.nikPay.Controller;

import com.example.nikPay.Config.JwtUtil;
import com.example.nikPay.Config.TokenResponse;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.Model.User;
import com.example.nikPay.Service.WalletService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        String token = jwtUtil.generateToken(savedUser.getUserID(), 3600000); // Generate token with 1 hour expiration
        return ResponseEntity.ok(new TokenResponse(token));
    }
    @GetMapping("/user")
    public ResponseEntity<User> getUserById(  @RequestHeader("token")String token){
        if (jwtUtil.verifyToken(token)) {
            Claims claims = jwtUtil.parseToken(token);
            String userID = claims.getSubject();
            User user  = userService.getUser(userID);
            return ResponseEntity.ok(user);
        }else {
            // Token verification failed, return unauthorized access response
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/user/verify")
    public ResponseEntity<Boolean> signIn(@RequestBody User user) {
        try {
            boolean isSignedIn = userService.checkPassword(user.getEmail(), user.getPassword());
            if(!isSignedIn){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }
            return ResponseEntity.ok(isSignedIn);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
