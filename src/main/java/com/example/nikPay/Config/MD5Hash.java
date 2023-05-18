package com.example.nikPay.Config;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {

    public static String getMD5Hash(String input) {
        try {
            // Create an instance of MessageDigest with MD5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the input string to bytes
            byte[] inputBytes = input.getBytes();

            // Calculate the MD5 hash
            byte[] hashBytes = md.digest(inputBytes);

            // Convert the hash bytes to a hexadecimal string
            BigInteger hashBigInt = new BigInteger(1, hashBytes);
            String hashString = hashBigInt.toString(16);

            // Pad the hash string with leading zeros if needed
            while (hashString.length() < 32) {
                hashString = "0" + hashString;
            }

            return hashString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


}
