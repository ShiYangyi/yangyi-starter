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

//对这个类进行测试，类似于service层中方法一样写单元测试，通过new这个类对象，去写单元测试。
//没有@Component注解，那么在启动程序时就不能发现这个类
//不需要flyway的配置类，使用默认配置即可
@Component
@RequiredArgsConstructor
//在官网给出的是继承一个已经废弃的类，废弃的类就不要使用了，由于废弃类实现了Callback接口，所以这里替换为实现Callback接口，
// 由于实现Callback接口后，接口里面的方法也需要重写，只所以还需要实现ApplicationContextAware接口，是因为为了避免循环依赖，
// 这里引入applicationContext来引入userSErvice/userRepository等需要的Bean
public class AfterMigrateCallback implements Callback, ApplicationContextAware {

    //最好不要使用@Autowired注入对象，虽然说应该能注入成功，但是缺点在于写测试时不方便，不能new对象，而且这里写了@Autowired后IDEA也会给出提示，最好修改成构造器的形式。
    private ApplicationContext applicationContext;
    //private Log log = LogFactory.getLog(getClass());

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

        //为了解决循环依赖，所以不通过Autowired的方式来注入UserRepository。而是选择借助applicationContext来获取UserRepository，
        //这种方式和Autowired的方式本质是一样的，所以通过Aotowired的对象都是通过UserRepository来管理的。所以下面这条语句与@Autowired方式注入得到的userRepository是等同的。
        UserRepository userRepository= applicationContext.getBean(UserRepository.class);
        UserService userService = applicationContext.getBean(UserService.class);
        BCryptPasswordEncoder bCryptPasswordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);

        //这个需要单独配置才能在终端打印出来，而且这里在终端显示的是info，不是debug层，所以这里可以试试改成log.info()。打断点是可以看到能跑到这个位置的。
        //log.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        for (User user : userRepository.findAll()) {
            if ("ROLE_MANAGER".equals(user.getRole())) {
                return;
            }
        }
        User user = User.builder().name("manager").password(bCryptPasswordEncoder.encode("manager")).role("ROLE_MANAGER").build();
        userService.register(user);
        //log.info("> afterEachMigrate");
    }

    //下面这个方法就是给applicationContext对象赋值。在测试方法中调用这个方法，applicationContext才不会为空
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
