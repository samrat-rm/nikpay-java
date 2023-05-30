package com.example.nikPay.Controller;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Service.TransferRecordService;
import com.example.nikPay.Service.UserService;
import com.example.nikPay.TransactionType;
import com.example.nikPay.Config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class TransferRecordsController {
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/transactions/sender/{userId}")
    public ResponseEntity<List<TransferRecords>> getSenderTransactions(
            @PathVariable String userId,
            @RequestParam TransactionType transactionType,
            @RequestHeader("token") String token
    ) {

        if (!jwtUtil.verifyToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!userService.isValidUserId(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        List<TransferRecords> transactions;
        if (transactionType == TransactionType.CREDIT) {
            transactions = transferRecordService.getSenderTransactions(userId);
        } else {
            transactions = transferRecordService.getReceiverTransactions(userId);
        }
        return ResponseEntity.ok(transactions);
    }
}