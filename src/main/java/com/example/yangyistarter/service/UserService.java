package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.LoginResponse;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.JwtLoginUtil;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

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

    /*public ResponseCode login(User user, String encryptedPassword) {
        for (User curUser : userRepository.findAll()) {
            if (curUser.getName().equals(user.getName())) {
                if(curUser.getPassword().equals(encryptedPassword)) {
                    return ResponseCode.USER_LOGIN_SUCCESS;
                }else{
                    return ResponseCode.ERROR_PASSWORD;
                }
            }
        }
        return ResponseCode.USER_DOES_NOT_EXIST;

    }*/

    public LoginResponse login(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //这里不要用JSONObject类，因为现在字段少，所以这样写可以，但是字段多就不适合了，所以最好是新建一个返回类，把需要的东西作为字段填充进去
        LoginResponse loginResponse = new LoginResponse();
        /*JSONObject jsonObject = new JSONObject();*/

        User userForBase = findUserByName(user);
        /*User userForBase = findUserById(user.getId());*/
        if (userForBase == null) {
            loginResponse.setMessage("登录失败,用户不存在");
        } else {
            if(!bCryptPasswordEncoder.matches(user.getPassword(), userForBase.getPassword())) {
            //if (!userForBase.getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()))) {
                loginResponse.setMessage("登录失败,密码错误");
            } else {
                String token = JwtLoginUtil.getToken(userForBase);
                loginResponse.setToken(token);
                loginResponse.setUser(userForBase);
                loginResponse.setMessage("登陆成功");
            }
        }

        return loginResponse;
    }

    public User findUserByName(User user) {

        for (User curUser : userRepository.findAll()) {
            if (curUser.getName().equals(user.getName())) {
                return curUser;
            }
        }
        return null;
    }

    public User findUserById(BigInteger userId) {
        return userRepository.getById(userId);
    }
}
