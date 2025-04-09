package com.example.chatapp;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String secretString = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("JWT Secret: " + secretString);
    }
}