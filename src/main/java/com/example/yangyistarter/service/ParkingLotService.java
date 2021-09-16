package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    ParkingLotRepository parkingLotRepository;
    UserService userService;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, UserService userService) {
        this.parkingLotRepository = parkingLotRepository;
        this.userService = userService;
    }

    public ResponseCode addParkingLot(ParkingLot parkingLot) {
        for (ParkingLot curParkingLot : parkingLotRepository.findAll()) {
            if (curParkingLot.getName().equals(parkingLot.getName())) {
                return ResponseCode.PARKINGLOT_ALREADY_EXISTS;
            }
        }
        parkingLotRepository.save(parkingLot);
        return ResponseCode.PARKINGLOT_ADD_SUCCESS;
    }

}
