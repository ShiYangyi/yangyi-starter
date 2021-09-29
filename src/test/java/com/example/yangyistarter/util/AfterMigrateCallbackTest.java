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
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        afterMigrateCallback.setApplicationContext(applicationContext);
        afterMigrateCallback.handle(event, context);
        verify(userService, times(1)).register(any());
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
        verify(userService, times(1)).register(any());
    }
}
