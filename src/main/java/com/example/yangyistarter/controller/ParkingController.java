package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.User;
import com.example.yangyistarter.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ParkingController {

    ParkingService parkingService;

    @PostMapping("parking")
    public long parking(@AuthenticationPrincipal User user) {
        return parkingService.parking(user);
    }
}
