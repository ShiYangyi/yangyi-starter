package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ManagerServiceTest {
    UserService userService = mock(UserService.class);
    UserRepository userRepository = mock(UserRepository.class);
    ManagerService managerService = new ManagerService(userService, userRepository);

    @Test
    //mock对象访问接口是在controller层测试的，所以写在这里是赘余
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_manager() {

        //given
        User manager = User.builder().name("syyyy").role("ROLE_MANAGER").build();
        //when
        when(userRepository.save(manager)).thenReturn(manager);
        //写这个方法的测试应该不需要去管verifyDuplicate()方法的内部实现对吗，为什么还会报错呢，说是managerRepository为空，定位到findAll()。
        //下面这条语句，managerService并不是mock的对象，是通过new出来的，所以下面语句的写法是错的，不是mock的对象并不能写成when().thenReturn()。
        //但是managerService又必须不能是mock的对象，因为managerService.addUser(user)是需要测试的方法
        //when(managerService.verifyDuplicate(user)).thenReturn(null);
        //when(userRepository.findAll()).thenReturn(Collections.singletonList(curUser));
        when(userService.findUserByName(manager.getUsername())).thenReturn(null);
        //then
        Assertions.assertEquals("user registered successful", managerService.addUser(manager).getMessage());

    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_clever_assistant() {
        //given
        User cleverAssistant = User.builder().name("syyyy").role("ROLE_CLEVER_ASSISTANT").build();
        //when
        when(userRepository.save(cleverAssistant)).thenReturn(cleverAssistant);
        when(userService.findUserByName(cleverAssistant.getUsername())).thenReturn(null);
        //then
        Assertions.assertEquals("user registered successful", managerService.addUser(cleverAssistant).getMessage());

    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_stupid_assistant() {
        //given
        User stupidAssistant = User.builder().name("syyyy").role("ROLE_STUPID_ASSISTANT").build();
        //when
        when(userRepository.save(stupidAssistant)).thenReturn(stupidAssistant);
        when(userService.findUserByName(stupidAssistant.getUsername())).thenReturn(null);
        //then
        Assertions.assertEquals("user registered successful", managerService.addUser(stupidAssistant).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_add_user_when_role_is_user() {
        //given
        User user = User.builder().name("syyyy").role("ROLE_USER").build();
        //when
        when(userRepository.save(user)).thenReturn(user);
        when(userService.findUserByName(user.getUsername())).thenReturn(null);
        //then
        Assertions.assertEquals("invalid request", managerService.addUser(user).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_add_user_failed_when_role_is_duplicate() {
        //given
        User stupidAssistant = User.builder().name("syyyy").role("ROLE_STUPID_ASSISTANT").build();
        //when
        when(userRepository.save(stupidAssistant)).thenReturn(stupidAssistant);
        when(userService.findUserByName(stupidAssistant.getUsername())).thenReturn(stupidAssistant);
        //then
        Assertions.assertEquals("user already exists", managerService.addUser(stupidAssistant).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_return_invalid_request_when_role_is_manager() {

        //given，这里user用户对象最好是用new，而不用mock
        User manager = User.builder().name("syyyy").role("ROLE_MANAGER").build();
        //when
        when(userRepository.save(manager)).thenReturn(manager);
        when(userService.findUserByName(manager.getUsername())).thenReturn(manager);
        //注意上面的when语句是mock的getUsername()方法，不是getName()方法，所以，现在在判断语句中也是使用getUsername()而不是getName()，要统一起来
        Assertions.assertEquals("invalid request", managerService.deleteUser(manager.getUsername()).getMessage());

    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_delete_user_when_role_is_clever_assistant() {

        //given
        User cleverAssistant = User.builder().name("syyyy").role("ROLE_CLEVER_ASSISTANT").build();
        //when
        when(userService.findUserByName(cleverAssistant.getUsername())).thenReturn(cleverAssistant);
        //then
        Assertions.assertEquals("user delete successful", managerService.deleteUser(cleverAssistant.getUsername()).getMessage());
        verify(userRepository).delete(any());

    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_delete_user_when_role_is_stupid_assistant() {

        //given
        User stupidAssistant = User.builder().name("syyyy").role("ROLE_STUPID_ASSISTANT").build();
        //when
        when(userService.findUserByName(stupidAssistant.getUsername())).thenReturn(stupidAssistant);
        //then
        Assertions.assertEquals("user delete successful", managerService.deleteUser(stupidAssistant.getUsername()).getMessage());
        verify(userRepository).delete(any());

    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_return_invalid_request_when_role_is_user() {
        //given
        //getName(),getUsername
        User user = User.builder().name("syyyy").role("ROLE_USER").build();
        //when
        when(userService.findUserByName(user.getName())).thenReturn(user);
        //then
        Assertions.assertEquals("invalid request", managerService.deleteUser(user.getName()).getMessage());
    }

    @Test
    //会自动补齐前缀ROLE_
    //@WithMockUser(roles = {"MANAGER"})
    public void should_delete_stupid_assistant_failed_when_user_is_not_exist() {
        //given
        User stupidAssistant = User.builder().name("syyyyy").role("ROLE_STUPID_ASSISTANT").build();
        //when
        when(userService.findUserByName(stupidAssistant.getName())).thenReturn(null);
        //then
        Assertions.assertEquals("user not exist", managerService.deleteUser(stupidAssistant.getName()).getMessage());
    }

}
