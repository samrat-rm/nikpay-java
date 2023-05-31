package com.example.nikPay.Controller;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Service.TransferRecordService;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.Config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Collections;
import java.util.List;

@RestController
public class TransferRecordsController {
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/transactions")
    public ResponseEntity<List<TransferRecords>> getSenderTransactions(
            @RequestHeader("token") String token
    ) {

        if (!jwtUtil.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = userService.getUserIDFromToken(token);

        if (!userService.isValidUserId(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<TransferRecords> transactions = transferRecordService.getTransactions(userId);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(transactions);
    }
}