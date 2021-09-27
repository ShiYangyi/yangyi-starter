package com.example.yangyistarter.service;

import com.example.yangyistarter.dto.UserDTO;
import com.example.yangyistarter.entity.LoginResponse;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import com.example.yangyistarter.util.SecurityConstants;
import com.example.yangyistarter.util.UserToken;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    /*如果是通过@Autowired注入的对象，在写测试时，是空的，如果要初始化，也是要通过构造函数，所以还不能直接在定义类的时候就用构造函数的形式来引入属性，
    这样在测试时这些内部对象就不会为空
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;*/

    public ResponseCode register(User user) {
        //不使用findAll()效率低，改为findByName()
        User curUser = findUserByName(user.getUsername());
        if (curUser != null) {
            return ResponseCode.USER_ALREADY_EXISTS;
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

    public LoginResponse login(UserDTO userDTO) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //这里不要用JSONObject类，因为现在字段少，所以这样写可以，但是字段多就不适合了，所以最好是新建一个返回类，把需要的东西作为字段填充进去
        LoginResponse loginResponse = new LoginResponse();

        //User user = User.builder().name(userDTO.getName()).password(userDTO.getPassword()).build();
        User user = findUserByName(userDTO.getName());
        if (user == null) {
            loginResponse.setMessage("登录失败,用户不存在");
        } else if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            loginResponse.setMessage("登录失败,密码错误");
        }else {
            UserToken userToken = new UserToken(user.getId().toString(), SecurityConstants.SECRET);
            String token = userToken.getToken();
            /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String token = JWTLoginFilter.getToken(authentication);*/
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
        //不要使用getById()，改用findById()
        return userRepository.findById(userId);
    }

    //获取当前登陆的用户，这个方法现在没有使用到
    /*public User getCurUser() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            //把Object类强转为Optional<User>
            user = (User) authentication.getPrincipal();
        }
        return user;
    }*/
}
