package com.example.yangyistarter.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private User user;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(User user) {
        this.user = user;
    }
}
