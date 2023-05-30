package com.example.nikPay.Service;

import com.example.nikPay.Currency;
import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Repository.TransferRecordsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferRecordService {

    private final TransferRecordsRepo transferRecordsRepo;

    @Autowired
    public TransferRecordService(TransferRecordsRepo transferRecordRepository) {
        this.transferRecordsRepo = transferRecordRepository;
    }

    public String saveTransferRecord(String senderId, String receiverId, float amount, Currency currency) {
        TransferRecords transferRecord = new TransferRecords(amount, senderId, receiverId);
        transferRecord.setCurrency(String.valueOf(currency));
        TransferRecords savedRecord = transferRecordsRepo.save(transferRecord);
        return savedRecord.getId().toString();
    }
}
