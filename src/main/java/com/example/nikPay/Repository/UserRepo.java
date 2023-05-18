package com.example.nikPay.Repository;

import com.example.nikPay.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository <User , Integer> {
    User findByEmail(String email);

    User findByUserID(String userID);
}
