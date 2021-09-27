package com.example.yangyistarter.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@AllArgsConstructor
public class UserToken {
    private String userId;
    private String password;

    public String getToken() {
        return Jwts.builder()
                .claim("userId", userId)
                .setExpiration(Date.from(Instant.now().plus(SecurityConstants.EXPIRES, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes(Charset.defaultCharset()))
                .compact();
    }
}










