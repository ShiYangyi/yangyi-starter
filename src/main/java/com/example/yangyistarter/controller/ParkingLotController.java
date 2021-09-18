package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.service.ParkingLotService;
import com.example.yangyistarter.util.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/delete")
    //测试这个接口时需要带上token，否则是无法验证用户身份的
    public ResponseCode deleteParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.deleteParkingLot(parkingLot);
    }
}
