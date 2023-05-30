package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.TransferRecordsRepo;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferRecordService {

    @Autowired
    private  TransferRecordsRepo transferRecordsRepo;

    @Autowired
    private WalletRepo walletRepo;


    public String saveTransferRecord(String senderId, String receiverId, float amount, Currency currency) {
        TransferRecords transferRecord = new TransferRecords(amount, senderId, receiverId ,String.valueOf(currency) );
        TransferRecords savedRecord = transferRecordsRepo.save(transferRecord);
        return savedRecord.getId().toString();
    }

    public void saveTransferRecordsInWallet(String sender, String receiver, float amount, Currency senderCurrency) {
        String transactionID = saveTransferRecord(sender, receiver, amount, senderCurrency);
        if (transactionID == null) {
            throw new RuntimeException("Failed to save transfer record.");
        }
        Wallet senderWallet = walletRepo.findByUserID(sender);
        Wallet receiverWallet = walletRepo.findByUserID(receiver);

        senderWallet.addTransactionId(transactionID);
        receiverWallet.addTransactionId(transactionID);

        walletRepo.save(senderWallet);
        walletRepo.save(receiverWallet);
    }

    public List<TransferRecords> getSenderTransactions(String senderId) {
        return transferRecordsRepo.findBySender(senderId);
    }

    public List<TransferRecords> getReceiverTransactions(String receiverId) {
        return transferRecordsRepo.findByReceiver(receiverId);
    }

}

