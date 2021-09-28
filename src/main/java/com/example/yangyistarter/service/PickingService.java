package com.example.yangyistarter.service;

import com.example.yangyistarter.entity.ParkingSpace;
import com.example.yangyistarter.repository.ParkingSpaceRepository;
import com.example.yangyistarter.util.ResponseCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PickingService {

    ParkingSpaceRepository parkingSpaceRepository;

    public ResponseCode picking(long receiptId) {
        Optional<ParkingSpace> parkingSpace = parkingSpaceRepository.findById(receiptId);
        if (!parkingSpace.isPresent()) {
            return ResponseCode.RECEIPT_INVALID;
        }
        if (!parkingSpace.get().getIsUsed()) {
            return ResponseCode.PARKING_SPACE_INVALID;
        }
        parkingSpace.get().setIsUsed(false);
        parkingSpaceRepository.save(parkingSpace.get());
        return ResponseCode.PICK_UP_CAR_SUCCESSFUL;
    }
}
