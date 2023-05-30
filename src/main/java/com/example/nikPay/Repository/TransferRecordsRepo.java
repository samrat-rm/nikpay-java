package com.example.nikPay.Repository;

import com.example.nikPay.Model.TransferRecords;
import com.example.nikPay.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRecordsRepo extends JpaRepository<TransferRecords, Integer> {
    List<TransferRecords> findBySender(String senderId);

    List<TransferRecords> findByReceiver(String receiverId);
}




