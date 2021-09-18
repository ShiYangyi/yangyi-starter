package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.service.ParkingLotService;
import com.example.yangyistarter.util.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parkinglot")
public class ParkingLotController {
    @Autowired
    ParkingLotService parkingLotService;

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/add")
    //测试这个接口时需要带上token，否则是无法验证用户身份的
    public ResponseCode addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.addParkingLot(parkingLot);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/delete/{name}")
    //测试这个接口时需要带上token，否则是无法验证用户身份的，传参不应该为ParkingLot对象，应该是String类型的名字
    public ResponseCode deleteParkingLot(@PathVariable String name) {
        return parkingLotService.deleteParkingLot(name);
    }
}
