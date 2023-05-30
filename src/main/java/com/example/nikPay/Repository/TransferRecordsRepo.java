package com.example.nikPay.Repository;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRecordsRepo extends JpaRepository<TransferRecords, Integer> {
    TransferRecords findBySender(String sender);

    TransferRecords findByReceiver(String receiver);
}




