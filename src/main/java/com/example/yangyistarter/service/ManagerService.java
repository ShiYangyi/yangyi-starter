package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {
    UserRepository userRepository;
    public ManagerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseCode addUser(User user) {
        if ("ROLE_CLEVER_ASSISTANT".equals(user.getRole()) || "ROLE_STUPID_ASSISTANT".equals(user.getRole()) ||
                "ROLE_MANAGER".equals(user.getRole())) {

            if(verifyDuplicate(user)) {
                return ResponseCode.USER_ALREADY_EXISTS;
            }
            userRepository.save(user);
            return ResponseCode.USER_REGISTER_SUCCESS;
        }
        return ResponseCode.INVALID_REQUEST;
    }

    //改造这个方法，让其返回值为布尔类型。
    public boolean verifyDuplicate(User user) {
        //如果数据库中数据很多，findAll()方法的性能不好。由于name具有唯一性，这里可以改用findByName()，简化代码。

        for (User curUser : userRepository.findAll()) {
            if (curUser.getName().equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

}
