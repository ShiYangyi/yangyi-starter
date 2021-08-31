package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseCode register(User user) {
        for (User curUser : userRepository.findAll()) {
            if (curUser.getName().equals(user.getName())) {
                return ResponseCode.USER_ALREADY_EXISTS;
            }
        }
        userRepository.save(user);
        return ResponseCode.USER_REGISTER_SUCCESS;
    }

}
