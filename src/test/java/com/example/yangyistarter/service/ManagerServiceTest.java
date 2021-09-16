package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Collections;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ManagerServiceTest {
    User user = mock(User.class);
    User curUser = mock(User.class);
    UserRepository userRepository = mock(UserRepository.class);
    ManagerService managerService = new ManagerService(userRepository);
    /*User manager = User.builder().id(BigInteger.valueOf(1111L)).name("yyyy").password("yyyyyy").role("ROLE_MANAGER").build();
    User cleverAssistant = User.builder().id(BigInteger.valueOf(1112L)).name("yyyX").password("yyyyyy").role("ROLE_CLEVER_ASSISTANT").build();
    User stupidAssistant = User.builder().id(BigInteger.valueOf(1112L)).name("yyyX").password("yyyyyy").role("ROLE_STUPID_ASSISTANT").build();
    User user = User.builder().id(BigInteger.valueOf(1113L)).name("yyyY").password("yyyyyy").role("ROLE_USER").build();*/

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_manager() {

        when(user.getRole()).thenReturn("ROLE_MANAGER");
        when(user.getName()).thenReturn("syyy");
        when(curUser.getName()).thenReturn("yyyy");
        when(userRepository.save(user)).thenReturn(user);

        //写这个方法的测试应该不需要去管verifyDuplicate()方法的内部实现对吗，为什么还会报错呢，说是managerRepository为空，定位到findAll()。
        //下面这条语句，managerService并不是mock的对象，是通过new出来的，所以下面语句的写法是错的，不是mock的对象并不能写成when().thenReturn()。
        //但是managerService又必须不能是mock的对象，因为managerService.addUser(user)是需要测试的方法
        //when(managerService.verifyDuplicate(user)).thenReturn(null);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(curUser));
        Assertions.assertEquals("user registered successful", managerService.addUser(user).getMessage());

    }

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_clever_assistant() {
        when(user.getRole()).thenReturn("ROLE_CLEVER_ASSISTANT");
        when(user.getName()).thenReturn("syyy");
        when(curUser.getName()).thenReturn("yyyy");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(curUser));
        Assertions.assertEquals("user registered successful", managerService.addUser(user).getMessage());

    }

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_stupid_assistant() {
        when(user.getRole()).thenReturn("ROLE_STUPID_ASSISTANT");
        when(user.getName()).thenReturn("syyy");
        when(curUser.getName()).thenReturn("yyyy");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(curUser));
        Assertions.assertEquals("user registered successful", managerService.addUser(user).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_user() {
        when(user.getRole()).thenReturn("ROLE_USER");
        when(user.getName()).thenReturn("syyy");
        when(curUser.getName()).thenReturn("yyyy");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(curUser));
        Assertions.assertEquals("invalid request", managerService.addUser(user).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    @WithMockUser(roles = {"MANAGER"})
    public void should_add_user_failed_when_role_is_duplicate() {
        when(user.getRole()).thenReturn("ROLE_STUPID_ASSISTANT");
        when(user.getName()).thenReturn("syyy");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        Assertions.assertEquals("user already exists", managerService.addUser(user).getMessage());
    }

}
