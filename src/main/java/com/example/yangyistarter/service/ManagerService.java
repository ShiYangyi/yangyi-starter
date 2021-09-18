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

        /*for (User curUser : userRepository.findAll()) {
            if (curUser.getName().equals(user.getName())) {
                return true;
            }
        }*/
        //如果想检测数据库中在姓名字段添加的唯一索引是否生效，可以循环1万次查询操作，看执行时间，另一种也可以通过explain指令来查看性能。
        if(userService.findUserByName(user.getUsername()) != null) {
            return true;
        }
        return false;
    }

    public ResponseCode deleteUser(String name) {
        User user = userService.findUserByName(name);
        if(user != null) {
            if ("ROLE_CLEVER_ASSISTANT".equals(user.getRole()) || "ROLE_STUPID_ASSISTANT".equals(user.getRole())) {
                userRepository.delete(user);
                return ResponseCode.USER_DELETE_SUCCESS;
            }
            return ResponseCode.INVALID_REQUEST;
        }
        return ResponseCode.USER_NOT_EXIST;
    }
}
