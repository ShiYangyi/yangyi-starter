package com.example.yangyistarter.util;
import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.service.UserService;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collections;
import static org.mockito.Mockito.*;

public class AfterMigrateCallbackTest {
    UserRepository userRepository = mock(UserRepository.class);
    UserService userService = mock(UserService.class);
    BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    Event event = mock(Event.class);
    Context context = mock(Context.class);
    ApplicationContext applicationContext = mock(ApplicationContext.class);
    AfterMigrateCallback afterMigrateCallback = new AfterMigrateCallback();

    @Test
    public void should_save_default_manager_when_empty_collection() {

        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);
        when(applicationContext.getBean(UserService.class)).thenReturn(userService);
        when(applicationContext.getBean(BCryptPasswordEncoder.class)).thenReturn(bCryptPasswordEncoder);
        //这条语句调用findAll()不能让它返回null，而是要返回空集合
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        //verify方法只能传入mock对象作为参数，在调用register()方法时，传入的参数mock对象和new对象都是可行的，
        // 这里疏漏的一点是根本没有去调用测试的方法，所以verify注册的方法是否执行，肯定是没有执行的。
        //如果不加下面这条语句，那么运行到实现方法时，applicationContext为空。
        afterMigrateCallback.setApplicationContext(applicationContext);
        afterMigrateCallback.handle(event, context);
        //注意这里register()传参数，不要写user，这样对象会不一致，写any()。
        verify(userService, times(1)).register(any());
        //下面方法是用来验证方法有没有执行，可能把参数改为any也行得通。
        //verify(userService).register(user);

    }

    @Test
    public void should_no_operation_when_have_manager() {
        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);
        when(applicationContext.getBean(UserService.class)).thenReturn(userService);
        when(applicationContext.getBean(BCryptPasswordEncoder.class)).thenReturn(bCryptPasswordEncoder);
        User user = mock(User.class);
        when(user.getRole()).thenReturn("ROLE_MANAGER");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        afterMigrateCallback.setApplicationContext(applicationContext);
        afterMigrateCallback.handle(event, context);
        //注意这里register()传参数，不要写user，这样对象会不一致，写any()。
        verify(userService, times(0)).register(any());
    }

    @Test
    public void should_save_default_manager_when_no_manager() {
        when(applicationContext.getBean(UserRepository.class)).thenReturn(userRepository);
        when(applicationContext.getBean(UserService.class)).thenReturn(userService);
        when(applicationContext.getBean(BCryptPasswordEncoder.class)).thenReturn(bCryptPasswordEncoder);
        User user = mock(User.class);
        when(user.getRole()).thenReturn("ROLE_USER");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        afterMigrateCallback.setApplicationContext(applicationContext);
        afterMigrateCallback.handle(event, context);
        //注意这里register()传参数，不要写user，这样对象会不一致，写any()。
        verify(userService, times(1)).register(any());
    }
}
