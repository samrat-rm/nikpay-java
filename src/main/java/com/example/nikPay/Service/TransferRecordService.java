package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.Wallet;
import com.example.nikPay.Repository.TransferRecordsRepo;
import com.example.nikPay.Repository.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferRecordService {

    private final TransferRecordsRepo transferRecordsRepo;

    @Autowired
    public TransferRecordService(TransferRecordsRepo transferRecordRepository) {
        this.transferRecordsRepo = transferRecordRepository;
    }

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
    }

}
