package com.example.yangyistarter.controller;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.UserService;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseCode register(@RequestBody @Valid UserDTO userDTO) {
        if (!"ROLE_USER".equals(userDTO.getRole())) {
            return ResponseCode.ROLE_PERMISSION_DENY;
        }
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return userService.register(User.builder().id(userDTO.getId()).name(userDTO.getName()).password(userDTO.getPassword()).role(userDTO.getRole()).build());
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Valid UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/messages")
    public String getMessage(@AuthenticationPrincipal User user) {
        return user.getName();
    }
}