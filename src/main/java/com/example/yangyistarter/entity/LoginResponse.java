package com.example.yangyistarter.entity;

import com.example.yangyistarter.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    //private User user;
    private UserDTO user;
    private String message;

    public LoginResponse() {
    }

    /*public LoginResponse(UserDTO user) {
        this.user = user;
    }*/
}
