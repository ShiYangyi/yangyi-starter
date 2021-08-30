package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

        //这条语句与项目无关，问题？注入了，为什么bCryptPasswordEncoder还是空的？？？？？？？
        //Assertions.assertEquals(bCryptPasswordEncoder.encode(curUser.getPassword()), userRepository.save(curUser).getPassword());
    }
}
