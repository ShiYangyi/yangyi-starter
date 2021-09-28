package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import com.example.yangyistarter.util.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ParkingLotService {

    ParkingLotRepository parkingLotRepository;
    ParkingSpaceRepository parkingSpaceRepository;
    UserService userService;

    public ParkingLot findParkingLotByName(String name) {
        Optional<ParkingLot> ParkingLot = parkingLotRepository.findByName(name);
        return ParkingLot.orElse(null);
    }

    public ResponseCode addParkingLot(ParkingLot parkingLot) {
        if (findParkingLotByName(parkingLot.getName()) != null) {
            return ResponseCode.PARKINGLOT_ALREADY_EXISTS;
        }
        parkingLotRepository.save(parkingLot);
        addParkingSpaces(parkingLot.getName());
        return ResponseCode.PARKINGLOT_ADD_SUCCESS;
    }

    private void addParkingSpaces(String parkingLotName) {
        for (int index = 0; index < 50; index++) {
            ParkingSpace parkingSpace = ParkingSpace.builder().isUsed(false).parkingLotName(parkingLotName).build();
            parkingSpaceRepository.save(parkingSpace);
            parkingSpace.setReceiptId(parkingSpace.getId());
            parkingSpaceRepository.save(parkingSpace);
        }
    }

    public ResponseCode deleteParkingLot(String parkingLotName) {
        ParkingLot curParkingLot = findParkingLotByName(parkingLotName);
        if (curParkingLot == null) {
            return ResponseCode.PARKINGLOT_NOT_EXIST;
        }
        parkingLotRepository.delete(curParkingLot);
        return ResponseCode.PARKINGLOT_DELETE_SUCCESS;
    }
}
