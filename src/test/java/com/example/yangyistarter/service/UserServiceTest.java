package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        UserService userService = new UserService(userRepository);
        ResponseCode responseCode = userService.register(curUser);

        Assertions.assertEquals("user already exists", responseCode.getMessage());

    }

    @Test
    public void should_save_user_when_different_name() {

        User user = User.builder().name("小明").password("1").build();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        UserService userService = new UserService(userRepository);
        ResponseCode responseCode = userService.register(curUser);

        Assertions.assertEquals("user registered successful", responseCode.getMessage());

    }
}
