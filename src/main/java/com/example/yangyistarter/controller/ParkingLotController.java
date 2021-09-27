package com.example.yangyistarter.controller;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.service.ParkingLotService;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParkingLotController {
    ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/parkinglot")
    public ResponseCode addParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotService.addParkingLot(parkingLot);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/parkinglot/{name}")
    public ResponseCode deleteParkingLot(@PathVariable String name) {
        return parkingLotService.deleteParkingLot(name);
    }
}
