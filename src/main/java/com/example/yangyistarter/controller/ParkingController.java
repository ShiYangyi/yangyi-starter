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

    //不要把接口和停车算法耦合，不好扩展
    @PostMapping("parking")
    public long parking(@AuthenticationPrincipal User user) {
        return parkingService.parking(user);
    }

    /*@PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/managers")
    public long randomParking() {
        return parkingService.randomParking();
    }

    @PreAuthorize("hasRole('ROLE_CLEVER_ASSISTANT')")
    @PostMapping("/clever/assistants")
    public long cleverParking() {
        return parkingService.cleverParking();
    }

    @PreAuthorize("hasAnyRole('ROLE_STUPID_ASSISTANT', 'ROLE_USER')")
    @PostMapping("/users")
    public long parking() {
        return parkingService.parking();
    }*/
}
