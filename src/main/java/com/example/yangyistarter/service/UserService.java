package com.example.yangyistarter.service;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.LoginResponse;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import com.example.yangyistarter.util.Constants;
import com.example.yangyistarter.util.UserToken;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;

    public ResponseCode register(User user) {
        User curUser = findUserByName(user.getUsername());
        if (curUser != null) {
            return ResponseCode.USER_ALREADY_EXISTS;
        }
        userRepository.save(user);
        return ResponseCode.USER_REGISTER_SUCCESS;
    }

    public LoginResponse login(UserDTO userDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        LoginResponse loginResponse = new LoginResponse();
        User user = findUserByName(userDTO.getName());
        if (user == null) {
            loginResponse.setMessage("登录失败,用户不存在");
        } else if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            loginResponse.setMessage("登录失败,密码错误");
        } else {
            UserToken userToken = new UserToken(user.getId().toString(), Constants.SECRET);
            String token = userToken.getToken();
            loginResponse.setToken(token);
            loginResponse.setUser(UserDTO.builder().name(user.getName()).build());
            loginResponse.setMessage("登陆成功");
        }
        return loginResponse;
    }

    public User findUserByName(String username) {
        Optional<User> user = userRepository.findByName(username);
        return user.orElse(null);
    }

    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }
}
