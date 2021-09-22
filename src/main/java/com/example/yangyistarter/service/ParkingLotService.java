package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingLot;
import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingLotRepository;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import com.example.yangyistarter.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    ParkingLotRepository parkingLotRepository;
    ParkingSpaceRepository parkingSpaceRepository;
    UserService userService;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingSpaceRepository parkingSpaceRepository, UserService userService) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.userService = userService;
    }

    public ResponseCode addParkingLot(ParkingLot parkingLot) {
        for (ParkingLot curParkingLot : parkingLotRepository.findAll()) {
            if (curParkingLot.getName().equals(parkingLot.getName())) {
                return ResponseCode.PARKINGLOT_ALREADY_EXISTS;
            }
        }
        parkingLotRepository.save(parkingLot);
        addParkingSpaces(parkingLot.getName());
        return ResponseCode.PARKINGLOT_ADD_SUCCESS;
    }

    private void addParkingSpaces(String name) {
        for (int index = 0; index < 50; index++) {
            ParkingSpace parkingSpace = ParkingSpace.builder().isUsed(false).parkingLotName(name).build();
            parkingSpaceRepository.save(parkingSpace);
            parkingSpace.setReceiptId(parkingSpace.getId());//因为没有repository的save操作之前，id值为空，所以这里set的receip_id值也为空，
            // 所以save到repository里receipt_id值也为空，如果在这条语句之前先添加一条save，那么存储到repository的receipt_id值不为空，但是这条语句之后，
            // 也还是需要一条save操作，这是因为循环到最后一轮时，如果不在这条语句后面添加save，可能导致最后这个循环的数据存储到repository的receipt_id值为空
            parkingSpaceRepository.save(parkingSpace);
        }
    }

    public ResponseCode deleteParkingLot(String name) {
        for (ParkingLot curParkingLot : parkingLotRepository.findAll()) {
            if (curParkingLot.getName().equals(name)) {
                parkingLotRepository.delete(curParkingLot);
                return ResponseCode.PARKINGLOT_DELETE_SUCCESS;
            }
        }
        return ResponseCode.PARKINGLOT_NOT_EXIST;
    }
}
