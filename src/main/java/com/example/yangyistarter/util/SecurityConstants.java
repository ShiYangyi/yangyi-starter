package com.example.yangyistarter.util;

public class SecurityConstants {
    private SecurityConstants() {
    }
    public static final String SECRET = "MyJwtSecret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int EXPIRES = 1000;
}