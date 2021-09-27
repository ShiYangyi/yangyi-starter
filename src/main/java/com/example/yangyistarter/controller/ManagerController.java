package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.ManagerService;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stuffs")
public class ManagerController {
    ManagerService managerService;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public ManagerController(ManagerService managerService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.managerService = managerService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping()
    public ResponseCode addStuffs(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return managerService.addStuffs(user);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{name}")
    public ResponseCode deleteStuffs(@PathVariable String name) {
        return managerService.deleteStuffs(name);
    }
}
