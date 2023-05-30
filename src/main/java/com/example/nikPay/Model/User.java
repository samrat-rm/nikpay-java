package com.example.nikPay.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("email")
    @Column(unique = true)
    private String email;

    @JsonProperty("password")
    private String password;

    private String firstName;
    private String lastName;

    private String userID = UUID.randomUUID().toString();

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateToHashPassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
