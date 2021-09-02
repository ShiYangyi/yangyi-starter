package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.LoginResponse;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    /*@MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;*/

    @MockBean
    private UserRepository userRepository;

    User curUser = User.builder().name("zly").password("2").build();

    @Test
    public void should_save_user_when_register() {

        when(userRepository.save(curUser)).thenReturn(curUser);
        Assertions.assertEquals("zly", userRepository.save(curUser).getName());
        Assertions.assertEquals("2", userRepository.save(curUser).getPassword());

        //这条语句与项目无关，注入了，为什么bCryptPasswordEncoder还是空的。
        //1.如果测试方法里面只有@MockBean没有@AutoWired注解，那么只需要在类上添加@ExtendWith(SpringExtension.class)，
        // 而不需要@SpringBootTest注解，因为加上@SpringBootTest注解运行会很慢，这个@SpringBootTest注解会启动容器中的很多东西。而如果测试方法中存在@AutoWired注解，
        // 那么就需要在类上添加@SpringBoot注解，否则很多东西就无法注入。
        //下面调试会发现bCryptPasswordEncoder这个mock对象并不是空的，之所以bCryptPasswordEncoder.encode(curUser.getPassword())为null，
        // 是因为mock对象并不会去执行encode()方法的实际逻辑，默认返回null。类似的，下面方法中userRepository.findAll()，
        // mock对象userRepository也不会去执行真正的findAll()方法的逻辑，when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        // 这条语句使得mock对象userRepository不会执行findAll()，而是指定该mock对象执行方法后返回Collections.singletonList(user)，表示只含有一个元素的列表。
        //Assertions.assertEquals(bCryptPasswordEncoder.encode(curUser.getPassword()), userRepository.save(curUser).getPassword());
    }

    @Test
    public void should_return_user_already_exists_when_same_name() {

        //这里思路，要测试返回ResponseCode.USER_ALREADY_EXISTS的逻辑，就是UserService中register()方法中的for循环部分，此时不需要考虑其他部分，
        // 例如for循环外部的save方法，这都是这个测试方法覆盖范围外的。所以第一步要找到真正需要测试的部分，这里找到了是for循环。
        //第二步，由于mock对象是userRepository，需要mock的方法是范围内的findAll()，所以写下下面的when()thenReturn()，
        // 由于findAll()方法返回的是含有User的列表，所以这里应该指定mock对象执行该方法后的返回值为含有和curUser同名的用户的列表，
        // 此时是不会真正执行findAll()。
        User user = User.builder().name("zly").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().name("zly").password(savedPassword).build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        UserService userService = new UserService(userRepository);
        ResponseCode responseCode = userService.register(curUser);

        Assertions.assertEquals("user already exists", responseCode.getMessage());

    }

    @Test
    public void should_save_user_when_different_name() {

        User user = User.builder().name("小明").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().name("小明").password(savedPassword).build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        UserService userService = new UserService(userRepository);
        ResponseCode responseCode = userService.register(curUser);

        Assertions.assertEquals("user registered successful", responseCode.getMessage());

    }

    @Test
    public void should_failed_when_login_user_not_exists() {

        User user = User.builder().name("zly").password("1").build();
        UserService userService = new UserService(userRepository);
        //下面写法错误，因为这里mock的对象是userRepository，userService不是mock的对象，所以调用findUserByName()是会真正执行逻辑的。
        // 正确的测试写法是：由于使用到findUserByName()，里面执行了userRepository.findAll()，所以正确写法是mock对象执行findAll()，期望返回空列表。
        //when(userService.findUserByName(user)).thenReturn(null);
        when(userRepository.findAll()).thenReturn(new ArrayList<User>(){});
        Assertions.assertEquals("登录失败,用户不存在", userService.login(user).getMessage());

    }

    @Test
    public void should_failed_when_login_error_password() {

        User user = User.builder().name("zly").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().name("zly").password(savedPassword).build();
        UserService userService = new UserService(userRepository);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        Assertions.assertEquals("登录失败,密码错误", userService.login(curUser).getMessage());
    }

    @Test
    public void should_login_when_password_correct() {

        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password(savedPassword).build();
        UserService userService = new UserService(userRepository);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        LoginResponse loginResponse = userService.login(user);
        Assertions.assertEquals("登陆成功", loginResponse.getMessage());
    }

    @Test
    public void should_return_user_when_given_valid_username() {

        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password(savedPassword).build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        UserService userService = new UserService(userRepository);
        Assertions.assertEquals(savedUser, userService.findUserByName(user));

    }

    @Test
    public void should_return_null_when_given_error_username() {
        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("po").password("1").build();
        String savedPassword = "$2a$10$lsWFF4FHNj2Y3dyKv1iCf.4pMytV4dPgfoVmdX18w3V3Q2lbW1/ae";//对应密码1
        User savedUser = User.builder().id(new BigInteger(String.valueOf(1111))).name("po").password(savedPassword).build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(savedUser));
        UserService userService = new UserService(userRepository);
        Assertions.assertNull(userService.findUserByName(curUser));
    }

    @Test
    public void should_return_null_when_given_invalid_username() {
        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("1").build();
        when(userRepository.findAll()).thenReturn(new ArrayList<User>(){});
        UserService userService = new UserService(userRepository);
        Assertions.assertNull(userService.findUserByName(user));
    }

    @Test
    public void should_return_user_when_given_valid_id() {

        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("1").build();
        when(userRepository.getById(user.getId())).thenReturn(user);
        UserService userService = new UserService(userRepository);
        Assertions.assertEquals(user, userService.findUserById(user.getId()));

    }

    @Test
    public void should_return_null_when_given_invalid_id() {
        User user = User.builder().id(new BigInteger(String.valueOf(1111))).name("zly").password("1").build();
        when(userRepository.getById(user.getId())).thenReturn(null);
        UserService userService = new UserService(userRepository);
        Assertions.assertNull(userService.findUserById(user.getId()));
    }
}
