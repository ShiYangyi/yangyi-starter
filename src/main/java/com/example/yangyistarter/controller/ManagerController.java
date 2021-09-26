package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.ManagerService;
import com.example.yangyistarter.util.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stuffs")
public class ManagerController {
    @Autowired
    ManagerService managerService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping()
    //测试这个接口时需要带上token，否则是无法验证用户身份的
    //这里用到了aop，用到了代理，代理是框架来实现的，因为通过postman传进来的是一个json，
    // json需要转换成对象，这些数据流的转换工作都是框架去实现的，在实现自己写的方法之前，
    // 框架做了这些工作，当在方法中打了断点，在执行到return语句时，再单步调试就会进入到框架的代理方法。
    // 代理方法相当于是在自己的实现方法外面包裹了一层。
    public ResponseCode addStuffs(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return managerService.addStuffs(user);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    //@PostMapping("/delete")
    //@GetMapping("/delete/{name}")
    @DeleteMapping("/{name}")
    //测试这个接口时需要带上token，否则是无法验证用户身份的
    //这里用到了aop，用到了代理，代理是框架来实现的，因为通过postman传进来的是一个json，
    // json需要转换成对象，这些数据流的转换工作都是框架去实现的，在实现自己写的方法之前，
    // 框架做了这些工作，当在方法中打了断点，在执行到return语句时，再单步调试就会进入到框架的代理方法。
    // 代理方法相当于是在自己的实现方法外面包裹了一层。
    /*public ResponseCode deleteUser(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return managerService.deleteUser(user);
    }*/
    public ResponseCode deleteStuffs(@PathVariable String name) {
        return managerService.deleteStuffs(name);
    }
}
