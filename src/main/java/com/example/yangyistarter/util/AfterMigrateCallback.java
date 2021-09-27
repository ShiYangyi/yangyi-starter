package com.example.yangyistarter.util;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.repository.UserRepository;
import com.example.yangyistarter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Context;
import org.flywaydb.core.api.callback.Event;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AfterMigrateCallback implements Callback, ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public boolean supports(Event event, Context context) {
        return event == Event.AFTER_MIGRATE;
    }

    @Override
    public boolean canHandleInTransaction(Event event, Context context) {
        return true;
    }

    @Override
    public void handle(Event event, Context context) {
        UserRepository userRepository = applicationContext.getBean(UserRepository.class);
        UserService userService = applicationContext.getBean(UserService.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
        for (User user : userRepository.findAll()) {
            if ("ROLE_MANAGER".equals(user.getRole())) {
                return;
            }
        }
        User user = User.builder().name("manager").password(bCryptPasswordEncoder.encode("manager")).role("ROLE_MANAGER").build();
        userService.register(user);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
