package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    UserService userService;
    UserRepository userRepository;

    public ManagerService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public ResponseCode addStuffs(User user) {
        if (roleIsAllowed(user, "add")) {
            if (userIsExist(user)) {
                return ResponseCode.USER_ALREADY_EXISTS;
            }
            userRepository.save(user);
            return ResponseCode.STAFF_ADD_SUCCESS;
        }
        return ResponseCode.ROLE_PERMISSION_DENY;
    }

    private boolean roleIsAllowed(User user, String flag) {
        return "add".equals(flag) ? "ROLE_CLEVER_ASSISTANT".equals(user.getRole()) || "ROLE_STUPID_ASSISTANT".equals(user.getRole()) ||
                "ROLE_MANAGER".equals(user.getRole()) : "ROLE_CLEVER_ASSISTANT".equals(user.getRole()) || "ROLE_STUPID_ASSISTANT".equals(user.getRole());
    }

    public boolean userIsExist(User user) {
        return userService.findUserByName(user.getUsername()) != null;
    }

    public ResponseCode deleteStuffs(String name) {
        User user = userService.findUserByName(name);
        if (user != null) {
            if (roleIsAllowed(user, "delete")) {
                userRepository.delete(user);
                return ResponseCode.STAFF_DELETE_SUCCESS;
            }
            return ResponseCode.ROLE_PERMISSION_DENY;
        }
        return ResponseCode.USER_NOT_EXIST;
    }
}
