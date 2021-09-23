package com.example.yangyistarter.controller;

import com.example.yangyistarter.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@AllArgsConstructor
@RequestMapping("parking")
public class ParkingController {

    ParkingService parkingService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/managers")
    public BigInteger randomParking() {
        return parkingService.randomParking();
    }

    @PreAuthorize("hasRole('ROLE_CLEVER_ASSISTANT')")
    @PostMapping("/clever/assistants")
    public BigInteger cleverParking() {
        return parkingService.cleverParking();
    }

    @PreAuthorize("hasAnyRole('ROLE_STUPID_ASSISTANT', 'ROLE_USER')")
    @PostMapping("/users")
    public BigInteger parking() {
        return parkingService.parking();
    }
}
